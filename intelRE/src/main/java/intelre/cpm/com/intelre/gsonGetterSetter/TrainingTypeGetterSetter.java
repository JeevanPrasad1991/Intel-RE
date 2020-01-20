
package intelre.cpm.com.intelre.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainingTypeGetterSetter {

    @SerializedName("Window_Master")
    @Expose
    private List<WindowMaster> windowMaster = null;

    public List<WindowMaster> getWindowMaster() {
        return windowMaster;
    }

    public void setWindowMaster(List<WindowMaster> windowMaster) {
        this.windowMaster = windowMaster;
    }

}
