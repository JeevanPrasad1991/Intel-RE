package intelre.cpm.com.intelre.multiselectionspin;

import java.util.ArrayList;

import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;

/**
 * Created by jeevanp on 2/2/2018.
 */

public interface SpinnerListener {
    void onItemsSelected(ArrayList<StoreCategoryMaster> items);
}