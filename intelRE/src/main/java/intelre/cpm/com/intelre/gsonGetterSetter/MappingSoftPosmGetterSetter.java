
package intelre.cpm.com.intelre.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappingSoftPosmGetterSetter {

    @SerializedName("Mapping_Soft_Posm")
    @Expose
    private List<MappingSoftPosm> mappingSoftPosm = null;

    public List<MappingSoftPosm> getMappingSoftPosm() {
        return mappingSoftPosm;
    }

    public void setMappingSoftPosm(List<MappingSoftPosm> mappingSoftPosm) {
        this.mappingSoftPosm = mappingSoftPosm;
    }

}
