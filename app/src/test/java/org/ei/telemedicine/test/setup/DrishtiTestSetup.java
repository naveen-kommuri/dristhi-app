//package org.ei.telemedicine.test.setup;
//
//import org.robolectric.bytecode.ClassInfo;
//import org.robolectric.bytecode.Setup;
//
//public class DrishtiTestSetup extends Setup {
//    @Override
//    public boolean shouldInstrument(ClassInfo classInfo) {
//        return super.shouldInstrument(classInfo)
//                || classInfo.getName().equals("org.ei.telemedicine.Context")
//                || classInfo.getName().equals("org.ei.telemedicine.view.controller.ECSmartRegisterController")
//                || classInfo.getName().equals("org.ei.telemedicine.view.controller.VillageController");
//    }
//}