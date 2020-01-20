
package intelre.cpm.com.intelre.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappingPermanentPosm {

    @SerializedName("Store_Id")
    @Expose
    private Integer storeId;
    @SerializedName("Posm_Id")
    @Expose
    private Integer posmId;

    private Integer posmTypeId;

    public Integer getPosmTypeId() {
        return posmTypeId;
    }

    public void setPosmTypeId(Integer posmTypeId) {
        this.posmTypeId = posmTypeId;
    }

    public String getPosmType() {
        return posmType;
    }

    public void setPosmType(String posmType) {
        this.posmType = posmType;
    }

    private String posmType;
    public Integer getPrev_Qty() {
        return prev_Qty;
    }

    public void setPrev_Qty(Integer prev_Qty) {
        this.prev_Qty = prev_Qty;
    }

    @SerializedName("Prev_Qty")
    @Expose
    private Integer prev_Qty;


    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getPosmId() {
        return posmId;
    }

    public void setPosmId(Integer posmId) {
        this.posmId = posmId;
    }

    public String getPosm() {
        return posm;
    }

    public void setPosm(String posm) {
        this.posm = posm;
    }
    private String posm;
    private String  preV_dValue;
    private String  newDeploymnt_Value;

    public String getPreV_dValue() {
        return preV_dValue;
    }

    public void setPreV_dValue(String preV_dValue) {
        this.preV_dValue = preV_dValue;
    }

    public String getNewDeploymnt_Value() {
        return newDeploymnt_Value;
    }

    public void setNewDeploymnt_Value(String newDeploymnt_Value) {
        this.newDeploymnt_Value = newDeploymnt_Value;
    }

    public String getsPermanetIMG_1() {
        return sPermanetIMG_1;
    }

    public void setsPermanetIMG_1(String sPermanetIMG_1) {
        this.sPermanetIMG_1 = sPermanetIMG_1;
    }

    public String getsPermanetIMG_2() {
        return sPermanetIMG_2;
    }

    public void setsPermanetIMG_2(String sPermanetIMG_2) {
        this.sPermanetIMG_2 = sPermanetIMG_2;
    }

    public String getsPermanetIMG_3() {
        return sPermanetIMG_3;
    }

    public void setsPermanetIMG_3(String sPermanetIMG_3) {
        this.sPermanetIMG_3 = sPermanetIMG_3;
    }

    private String sPermanetIMG_1,sPermanetIMG_2,sPermanetIMG_3;


}
