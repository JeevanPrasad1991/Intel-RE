
package intelre.cpm.com.intelre.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoTypeMaster {

    @SerializedName("Info_Type_Id")
    @Expose
    private Integer infoTypeId;
    @SerializedName("Info_Type")
    @Expose
    private String infoType;

    private String brand;
    private String  brand_cd;
    private String type;
    private String type_cd;
    private String remark;

    public boolean isExistsFlag() {
        return existsFlag;
    }

    public void setExistsFlag(boolean existsFlag) {
        this.existsFlag = existsFlag;
    }

    private boolean existsFlag=true;

    public String  getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    String key_id;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand_cd() {
        return brand_cd;
    }

    public void setBrand_cd(String brand_cd) {
        this.brand_cd = brand_cd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_cd() {
        return type_cd;
    }

    public void setType_cd(String type_cd) {
        this.type_cd = type_cd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMarketinfo_img() {
        return marketinfo_img;
    }

    public void setMarketinfo_img(String marketinfo_img) {
        this.marketinfo_img = marketinfo_img;
    }

    private String marketinfo_img;


    public Integer getInfoTypeId() {
        return infoTypeId;
    }

    public void setInfoTypeId(Integer infoTypeId) {
        this.infoTypeId = infoTypeId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

}
