
package intelre.cpm.com.intelre.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoTypeMasterGetterSetter {

    @SerializedName("Info_Type_Master")
    @Expose
    private List<InfoTypeMaster> infoTypeMaster = null;

    public List<InfoTypeMaster> getInfoTypeMaster() {
        return infoTypeMaster;
    }

    public void setInfoTypeMaster(List<InfoTypeMaster> infoTypeMaster) {
        this.infoTypeMaster = infoTypeMaster;
    }

}
