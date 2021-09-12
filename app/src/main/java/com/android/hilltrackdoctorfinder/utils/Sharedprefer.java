package com.android.hilltrackdoctorfinder.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Sharedprefer {
    Context context;
    public SharedPreferences default_prefence;

    public Sharedprefer(Context context) {
        this.context = context;
        default_prefence = context.getSharedPreferences("doctor_finder", Context.MODE_PRIVATE);
    }


    public String getUsername() {
        return default_prefence.getString("username", "");
    }

    public void setUsername(String username) {
        default_prefence.edit().putString("username", username).apply();
    }

    public String getFirst_name() {
        return default_prefence.getString("user_first_name", "");
    }

    public void setFirst_name(String first_name) {
        default_prefence.edit().putString("user_first_name", first_name).apply();
    }

    public String getLast_name() {
        return default_prefence.getString("user_last_name", "");
    }

    public void setLast_name(String last_name) {
        default_prefence.edit().putString("user_last_name", last_name).apply();
    }

    public String getMobile_number() {
        return default_prefence.getString("user_mobile_number", "");
    }

    public void setMobile_number(String mobile_number) {
        default_prefence.edit().putString("user_mobile_number", mobile_number).apply();
    }

    public String getEmail() {
        return default_prefence.getString("user_email", "");
    }

    public void setEmail(String email) {
        default_prefence.edit().putString("user_email", email).apply();
    }

    public String getRecentNoInternet_page() {

        return default_prefence.getString("internet_error_last_page", "");
    }

    public void setRecentNoInternet_page(String activity) {
        default_prefence.edit().putString("internet_error_last_page", activity).apply();
    }

    public String getUserId() {

        return default_prefence.getString("user_ID", "");
    }

    public void setUserId(String activity) {
        default_prefence.edit().putString("user_ID", activity).apply();
    }


    public String getUser_profile_picture() {
        return default_prefence.getString("user_profile_picture", "");
    }

    public void setUser_profile_picture(String activity) {
        default_prefence.edit().putString("user_profile_picture", activity).apply();
    }


    public String getStatus() {

        return default_prefence.getString("status", "");
    }

    public void setStatus(String activity) {
        default_prefence.edit().putString("status", activity).apply();
    }

    public String getProfile_verification() {
        return default_prefence.getString("profile_verification", "");
    }

    public void setProfile_verification(String profile_verification) {
        default_prefence.edit().putString("profile_verification", profile_verification).apply();
    }


    public String getFirstSmsSendDate() {
        return default_prefence.getString("FirstSmsSend", "");
    }

    public void setFirstSmsSendDate(String profile_verification) {
        default_prefence.edit().putString("FirstSmsSend", profile_verification).apply();
    }


    public String getOTPSendNumber() {
        return default_prefence.getString("SendCodeNumber", "");
    }

    public void setOTPSendNumber(String profile_verification) {
        default_prefence.edit().putString("SendCodeNumber", profile_verification).apply();
    }


    public String getVerificationCode() {
        return default_prefence.getString("VerificationCode", "");
    }

    public void setVerificationCode(String profile_verification) {
        default_prefence.edit().putString("VerificationCode", profile_verification).apply();
    }


    public String getRemainingTime() {
        return default_prefence.getString("RemainingTime", "");
    }

    public void setRemainingTime(String profile_verification) {
        default_prefence.edit().putString("RemainingTime", profile_verification).apply();
    }


    public String getResendCode() {
        return default_prefence.getString("ResendCode", "");
    }

    public void setResendCode(String profile_verification) {
        default_prefence.edit().putString("ResendCode", profile_verification).apply();
    }


    public String getSeenNotificationList() {
        return default_prefence.getString("seenNotificationList", "");
    }

    public void setSeenNotificationList(String seenNotificationList) {
        default_prefence.edit().putString("seenNotificationList", seenNotificationList).apply();
    }


    public String getFatherName() {
        return default_prefence.getString("fatherName", "");
    }

    public void setFatherName(String fatherName) {
        default_prefence.edit().putString("fatherName", fatherName).apply();
    }

    public String getMotherName() {
        return default_prefence.getString("motherName", "");
    }

    public void setMotherName(String motherName) {
        default_prefence.edit().putString("motherName", motherName).apply();
    }


    public String getReligion() {
        return default_prefence.getString("religion", "");
    }

    public void setReligion(String religion) {
        default_prefence.edit().putString("religion", religion).apply();
    }


    public String getBirthDate() {
        return default_prefence.getString("birthDate", "");
    }

    public void setBirthDate(String birthDate) {
        default_prefence.edit().putString("birthDate", birthDate).apply();
    }


    public String getMaritial() {
        return default_prefence.getString("maritial", "");
    }

    public void setMaritial(String maritial) {
        default_prefence.edit().putString("maritial", maritial).apply();
    }


    public String getBloodGroup() {
        return default_prefence.getString("getBloodGroup", "");
    }

    public void setBloodGroup(String getBloodGroup) {
        default_prefence.edit().putString("getBloodGroup", getBloodGroup).apply();
    }

    public String getPermanentAdd() {
        return default_prefence.getString("getPermanentAdd", "");
    }

    public void setPermanentAdd(String getPermanentAdd) {
        default_prefence.edit().putString("getPermanentAdd", getPermanentAdd).apply();
    }


    public String getPresentAdd() {
        return default_prefence.getString("getPresentAdd", "");
    }

    public void setPresentAdd(String getPresentAdd) {
        default_prefence.edit().putString("getPresentAdd", getPresentAdd).apply();
    }

    public String getIdCard() {
        return default_prefence.getString("getIdCard", "");
    }

    public void setIdCard(String getIdCard) {
        default_prefence.edit().putString("getIdCard", getIdCard).apply();
    }


    public String getIdCardValue() {
        return default_prefence.getString("getIdCardValue", "");
    }

    public void setIdCardValue(String getIdCardValue) {
        default_prefence.edit().putString("getIdCardValue", getIdCardValue).apply();
    }


    public int getLocationSelectForIfterTiming() {
        return default_prefence.getInt("locationSelectForIfterTiming", 0);
    }

    public void setLocationSelectForIfterTiming(int locationSelectForIfterTiming) {
        default_prefence.edit().putInt("locationSelectForIfterTiming", locationSelectForIfterTiming).apply();
    }


    public int getActiveTabInHistory() {
        return default_prefence.getInt("ActiveTabInHistory", 0);
    }

    public void setActiveTabInHistory(int ActiveTabInHistory) {
        default_prefence.edit().putInt("ActiveTabInHistory", ActiveTabInHistory).apply();
    }

    public int getLocatoinRequest() {
        return default_prefence.getInt("locationRequestCall", 0);
    }

    public void locationRequestCall(int locationRequestCall) {
        default_prefence.edit().putInt("locationRequestCall", locationRequestCall).apply();
    }


    public Boolean userlogin() {
        return default_prefence.getBoolean("userslogin", false);
    }

    public void userlogin(Boolean userlogin) {
        default_prefence.edit().putBoolean("userslogin", userlogin).apply();
    }


    public Boolean homeDialogBannerStatus() {
        return default_prefence.getBoolean("homeDialogBannerStatus", false);
    }

    public void homeDialogBannerStatus(Boolean status) {
        default_prefence.edit().putBoolean("homeDialogBannerStatus", status).apply();
    }


    public Boolean firstTimeLaunch() {
        return default_prefence.getBoolean("launch", false);
    }

    public void firstTimeLaunch(Boolean launch) {
        default_prefence.edit().putBoolean("launch", launch).apply();
    }


    public String getLanguage() {
        return default_prefence.getString("Locale.Helper.Selected.Language", "en");
    }

    public void setLanguage(String language) {
        default_prefence.edit().putString("Locale.Helper.Selected.Language", language).apply();
    }


    public long getLastAdCheckinLog() {
        return default_prefence.getLong("last_ad_checkin_log", 0);
    }

    public void setLastAdCheckinLog(long last_ad_checkin_log) {
        default_prefence.edit().putLong("last_ad_checkin_log", last_ad_checkin_log).apply();
    }


    public String lastVerificationCode() {
        return default_prefence.getString("Vcode", "0");
    }

    public void lastVerificationCode(String code) {
        default_prefence.edit().putString("Vcode", code).apply();
    }


    public String getLoginPassword() {
        return default_prefence.getString("loginPassword", "0");
    }

    public void setLoginPassword(String password) {
        default_prefence.edit().putString("loginPassword", password).apply();
    }


    public String getAdminReferID() {
        return default_prefence.getString("adminReferID", "0");
    }

    public void setAdminReferID(String adminReferID) {
        default_prefence.edit().putString("adminReferID", adminReferID).apply();
    }


    public String getLinkGenAmount() {
        return default_prefence.getString("linkGenAmount", "500");
    }

    public void setLinkGenAmount(String linkGenAmount) {
        default_prefence.edit().putString("linkGenAmount", linkGenAmount).apply();
    }

    public String getMinTransferAmount() {
        return default_prefence.getString("minTransferAmount", "0");
    }

    public void setMinTransferAmount(String minTransferAmount) {
        default_prefence.edit().putString("minTransferAmount", minTransferAmount).apply();
    }


    public String getTransferCharge() {
        return default_prefence.getString("transferCharge", "0");
    }

    public void setTransferCharge(String transferCharge) {
        default_prefence.edit().putString("transferCharge", transferCharge).apply();
    }


    public String getVoucherPrice() {
        return default_prefence.getString("voucherPrice", "0");
    }

    public void setVoucherPrice(String voucherPrice) {
        default_prefence.edit().putString("voucherPrice", voucherPrice).apply();
    }


    public int getMinBankTransection() {
        return default_prefence.getInt("minBankTransectio", 0);
    }

    public void setMinBankTransection(int minBankTransectio) {
        default_prefence.edit().putInt("minBankTransectio", minBankTransectio).apply();
    }


    public int getAlertOkBtnClick() {
        return default_prefence.getInt("AlertOkBtnClick", 0);
    }

    public void setAlertOkBtnClick(int AlertOkBtnClick) {
        default_prefence.edit().putInt("AlertOkBtnClick", AlertOkBtnClick).apply();
    }


    public String getInstallReffer() {
        return default_prefence.getString("installReffer", "0");
    }

    public void setInstallReffer(String installReffer) {
        default_prefence.edit().putString("installReffer", installReffer).apply();
    }


    public boolean fundStatus() {
        return default_prefence.getBoolean("fundStatus", false);
    }

    public void fundStatus(boolean fundStatus) {
        default_prefence.edit().putBoolean("fundStatus", fundStatus).apply();
    }


    public String getFUND_STATUS_MESSAGE() {
        return default_prefence.getString("FUND_STATUS_MESSAGE", "0");
    }

    public void setFUND_STATUS_MESSAGE(String FUND_STATUS_MESSAGE) {
        default_prefence.edit().putString("FUND_STATUS_MESSAGE", FUND_STATUS_MESSAGE).apply();
    }


    public String getUser_password() {
        return default_prefence.getString("user_password", "0");
    }

    public void setUser_password(String user_password) {
        default_prefence.edit().putString("user_password", user_password).apply();
    }


    public boolean shopDirectlyLogin() {
        return default_prefence.getBoolean("shopDirectlyLogin", false);
    }

    public void shopDirectlyLogin(boolean shopDirectlyLogin) {
        default_prefence.edit().putBoolean("shopDirectlyLogin", shopDirectlyLogin).apply();
    }


    public Integer getSmsTiming() {
        return default_prefence.getInt("min", 0);
    }

    public void setSmsTiming(Integer min) {
        default_prefence.edit().putInt("min", min).apply();
    }


    public void clearAllData() {
        default_prefence.edit().clear().apply();
    }

}
