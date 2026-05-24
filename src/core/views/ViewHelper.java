package core.views;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import javax.swing.JOptionPane;

public final class ViewHelper {

    private ViewHelper() {
    }

    public static boolean handleResponse(java.awt.Component parent, Response response, Runnable onSuccess) {
        JOptionPane.showMessageDialog(parent, response.getMessage());
        if (response.getStatus() == StatusCode.OK || response.getStatus() == StatusCode.CREATED) {
            if (onSuccess != null) {
                onSuccess.run();
            }
            return true;
        }
        return false;
    }
}
