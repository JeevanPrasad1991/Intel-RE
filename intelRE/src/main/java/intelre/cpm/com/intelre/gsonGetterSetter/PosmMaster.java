
package intelre.cpm.com.intelre.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PosmMaster {

    @SerializedName("Posm_Id")
    @Expose
    private Integer posmId;
    @SerializedName("Posm")
    @Expose
    private String posm;
    @SerializedName("Posm_Type_Id")
    @Expose
    private Integer posmTypeId;
    @SerializedName("Posm_Type")
    @Expose
    private String posmType;

    public String deployment_Value;

    public String getDeployment_Value() {
        return deployment_Value;
    }

    public void setDeployment_Value(String deployment_Value) {
        this.deployment_Value = deployment_Value;
    }

    public String getSoft_merchIMG() {
        return soft_merchIMG;
    }

    public void setSoft_merchIMG(String soft_merchIMG) {
        this.soft_merchIMG = soft_merchIMG;
    }

    public String soft_merchIMG;
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

}
