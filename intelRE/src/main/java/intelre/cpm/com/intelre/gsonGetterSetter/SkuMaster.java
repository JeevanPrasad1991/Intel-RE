
package intelre.cpm.com.intelre.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SkuMaster {

    @SerializedName("Sku_Id")
    @Expose
    private Integer skuId;
    @SerializedName("Sku")
    @Expose
    private String sku;
    @SerializedName("Brand_Id")
    @Expose
    private Integer brandId;
    @SerializedName("Sku_Sequence")
    @Expose
    private Integer skuSequence;
    private String noof_display;
    private String machine_on;
    private String ipos;
    private String rxt;
    private String engegment;

    public String getCustomised_ipos() {
        return customised_ipos;
    }

    public void setCustomised_ipos(String customised_ipos) {
        this.customised_ipos = customised_ipos;
    }

    private String customised_ipos;

    public String getRxt() {
        return rxt;
    }

    public void setRxt(String rxt) {
        this.rxt = rxt;
    }

    public String getEngegment() {
        return engegment;
    }

    public void setEngegment(String engegment) {
        this.engegment = engegment;
    }

    public String getRxt_img() {
        return rxt_img;
    }

    public void setRxt_img(String rxt_img) {
        this.rxt_img = rxt_img;
    }

    private String rxt_img;


    public String getNoof_display() {
        return noof_display;
    }

    public void setNoof_display(String noof_display) {
        this.noof_display = noof_display;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    String key_id;

    public String getMachine_on() {
        return machine_on;
    }

    public void setMachine_on(String machine_on) {
        this.machine_on = machine_on;
    }

    public String getIpos() {
        return ipos;
    }

    public void setIpos(String ipos) {
        this.ipos = ipos;
    }

    public String getIpos_img() {
        return ipos_img;
    }

    public void setIpos_img(String ipos_img) {
        this.ipos_img = ipos_img;
    }

    private String ipos_img;

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getSkuSequence() {
        return skuSequence;
    }

    public void setSkuSequence(Integer skuSequence) {
        this.skuSequence = skuSequence;
    }
    String gaming_ipos="";

    public String getGaming_ipos() {
        return gaming_ipos;
    }

    public void setGaming_ipos(String gaming_ipos) {
        this.gaming_ipos = gaming_ipos;
    }
}
