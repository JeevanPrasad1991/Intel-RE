
package intelre.cpm.com.intelre.gsonGetterSetter;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllPosmTableGetterSetter {
    @SerializedName("Mapping_semi_permanent_posm")
    @Expose
    private List<MappingSemiPermanentPosm> mappingSemiPermanentPosm = null;

    public List<MappingSemiPermanentPosm> getMappingSemiPermanentPosm() {
        return mappingSemiPermanentPosm;
    }

    @SerializedName("Posm_Type_Question")
    @Expose
    private List<PosmTypeQuestion> posmTypeQuestion = null;

    public List<PosmTypeQuestion> getPosmTypeQuestion() {
        return posmTypeQuestion;
    }

    @SerializedName("Posm_Type")
    @Expose
    private List<PosmType> posmType = null;

    public List<PosmType> getPosmType() {
        return posmType;
    }

    @SerializedName("Mapping_SoftPosm")
    @Expose
    private List<MappingSoftPosm> mappingSoftPosm = null;

    public List<MappingSoftPosm> getMappingSoftPosm() {
        return mappingSoftPosm;
    }


    @SerializedName("Brave_Index_Score")
    @Expose
    private List<BraveIndexScore> braveIndexScore = null;

    public List<BraveIndexScore> getBraveIndexScore() {
        return braveIndexScore;
    }
}
