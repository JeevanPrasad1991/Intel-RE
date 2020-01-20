
package intelre.cpm.com.intelre.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PosmMasterGetterSetter {

    @SerializedName("Posm_Master")
    @Expose
    private List<PosmMaster> posmMaster = null;

    public List<PosmMaster> getPosmMaster() {
        return posmMaster;
    }

    public void setPosmMaster(List<PosmMaster> posmMaster) {
        this.posmMaster = posmMaster;
    }

}
