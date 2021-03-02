package siri.xlite.common;

import java.util.ListResourceBundle;

public class Messages extends ListResourceBundle {
    public static final String LOAD_FROM_BACKEND = "LOAD_FROM_BACKEND";
    public static final String REVALIDATE_RESSOURCE = "REVALIDATE_RESSOURCE";

    @Override
    protected Object[][] getContents() {
        return new Object[][]{{LOAD_FROM_BACKEND, "\u001B[1;31mload from backend : {}\u001B[0;39m"},
                {REVALIDATE_RESSOURCE, "\u001B[1;31mrevalidate ressource : {}\u001B[0;39m"},};
    }
}