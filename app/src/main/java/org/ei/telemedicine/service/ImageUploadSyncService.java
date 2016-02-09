package org.ei.telemedicine.service;

import android.util.Log;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.domain.EligibleCouple;
import org.ei.telemedicine.domain.ProfileImage;
import org.ei.telemedicine.domain.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static java.text.MessageFormat.format;
import static org.ei.telemedicine.util.Log.logError;

/**
 * Created by Dimas Ciputra on 3/22/15.
 */
public class ImageUploadSyncService {


    Context context;

    public ImageUploadSyncService() {
        context = Context.getInstance();
        ImageSync();
        getImages();
    }

    private void getImages() {
        String anmId = context.allSharedPreferences().fetchRegisteredANM();

        String imageGetURL = context.configuration().dristhiBaseURL() + "/multimedia-file?anm-id=" + anmId;
        Log.e("Image Getting url", imageGetURL);
        Response<String> response = context.getHttpAgent().fetch(imageGetURL);
        if (response.isFailure()) {
            logError(format("Fetching images url failed."));
        } else {
            try {
                JSONArray imagesArray = new JSONArray(response.payload());
                for (int i = 0; i < imagesArray.length(); i++) {
                    JSONObject imageJson = imagesArray.getJSONObject(i);
                    if (imageJson.getString("contentType").toLowerCase().contains("image"))
                        switch (imageJson.getString("fileCategory").toLowerCase()) {
                            case AllConstants.WOMAN_TYPE:
                                EligibleCouple byCaseID = context.allEligibleCouples().findByCaseID(getDataFromJson(imageJson.toString(), "caseId"));
                                if (null != byCaseID && (byCaseID.photoPath() == null || byCaseID.photoPath().trim().length() == 0)) {
                                    String localPath = context.getHttpAgent().saveImage(context.configuration().dristhiBaseURL() + "/" + getDataFromJson(imageJson.toString(), "filePath"));
                                    context.allEligibleCouples().updatePhotoPath(getDataFromJson(imageJson.toString(), "caseId"), localPath);
                                }
                                break;
                        }
//                    context.allEligibleCouples().updatePhotoPath(getDataFromJson(imageJson.toString(), "caseId"), context.configuration().dristhiBaseURL() + "/" + getDataFromJson(imageJson.toString(), "filePath"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private String getDataFromJson(String jsonStr, String key) {
        if (jsonStr != null && !jsonStr.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                return jsonObject.has(key) ? jsonObject.getString(key) : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public void ImageSync() {
        List<EligibleCouple> unSyncedProfilepics = context.allEligibleCouples().getUnSyncedProfilepics();
        for (EligibleCouple eligibleCouple : unSyncedProfilepics) {
            ProfileImage image = new ProfileImage("", context.allSharedPreferences().fetchRegisteredANM(), eligibleCouple.caseId(), "Image", eligibleCouple.photoPath(), "").withFileCategory(AllConstants.WOMAN_TYPE);
            String respone = context.getHttpAgent().httpImagePost(context.configuration().dristhiBaseURL() + "/multimedia-file", image);
            if (respone != null)
                context.allEligibleCouples().updatePhotoURL(eligibleCouple.caseId(), context.configuration().dristhiBaseURL() + "/" + context.allSharedPreferences().fetchRegisteredANM() + "/images/" + eligibleCouple.caseId() + ".jpg");
        }

//        for (int i = 0; i < profileimages.size(); i++) {
//            String response = context.getHttpAgent().httpImagePost(context.configuration().dristhiBaseURL() + "/multimedia-file", profileimages.get(i));
////            int RESPONSE_OK = 200;
////            int RESPONSE_OK_ = 201;
////
////            if (response != RESPONSE_OK_ && response != RESPONSE_OK) {
////            } else {
////                imagerepository.close(profileimages.get(i).getImageid());
////            }
//            if (response.trim().length() != 0)
//                imagerepository.close(profileimages.get(i).getImageid());
//        }
    }


}