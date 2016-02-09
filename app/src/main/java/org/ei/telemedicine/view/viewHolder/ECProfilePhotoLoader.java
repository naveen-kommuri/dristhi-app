package org.ei.telemedicine.view.viewHolder;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.ei.telemedicine.view.contract.SmartRegisterClient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.ei.telemedicine.AllConstants.DEFAULT_WOMAN_IMAGE_PLACEHOLDER_PATH;

public class ECProfilePhotoLoader implements ProfilePhotoLoader {
    private final Resources resources;
    private final Drawable defaultPlaceHolder;
    private final Map<String, Drawable> drawableMap = new HashMap<String, Drawable>();
    private Drawable asyncDrawable;

    public ECProfilePhotoLoader(Resources res, Drawable placeHolder) {
        this.resources = res;
        defaultPlaceHolder = placeHolder;
    }

    public Drawable get(SmartRegisterClient client) {
        if (drawableMap.containsKey(client.entityId())) {
            return drawableMap.get(client.entityId());
        }

        String photoPath = client.profilePhotoPath();
        android.util.Log.e("EC Profile Photoloader", photoPath + "-------" + client.wifeName());
        if (isBlank(photoPath)
                || isThisDefaultProfilePhoto(photoPath)
                || !isFileExists(photoPath)) {
            Log.e("default", photoPath);
            return defaultPlaceHolder;
        }

        Drawable profilePhoto = new BitmapDrawable(resources, photoPath.replace("file:///", "/"));
        drawableMap.put(client.entityId(), profilePhoto);
        return profilePhoto;
    }

    private boolean isFileExists(String path) {
        return new File(path.replace("file:///", "/")).exists() || isFromURL(path);
    }

    private boolean isThisDefaultProfilePhoto(String photoPath) {
        return photoPath.contains(DEFAULT_WOMAN_IMAGE_PLACEHOLDER_PATH);
    }

    private boolean isFromURL(String photoPath) {
        return photoPath.contains("http://");
    }

}
