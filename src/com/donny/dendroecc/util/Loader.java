package com.donny.dendroecc.util;

import com.donny.dendroecc.crypto.Registry;
import com.donny.dendroroot.fileio.FileHandler;
import com.donny.dendroroot.instance.Instance;
import com.donny.dendroroot.json.JsonObject;

import java.io.File;

public class Loader extends FileHandler {
    public Loader(Instance curInst) {
        super(curInst);
    }

    /**
     * loads curve JSON files from ~/data/stdcrv
     * a large number of these jsons are included.
     * the <code>characteristics</code> field is not read from, however all other fields are expected:
     * <code>name</code>, <code>desc</code>, <code>form</code>, <code>field</code>, <code>params</code>, <code>generator</code>, <code>order</code>, and <code>cofactor</code>
     */
    public void loadStandardCurves() {
        File dir = new File(CURRENT_INSTANCE.data.getAbsoluteFile() + File.separator + "stdcrv");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                JsonObject raw = (JsonObject) readJson(file);
                Registry.register(raw);
            }
        }
    }
}
