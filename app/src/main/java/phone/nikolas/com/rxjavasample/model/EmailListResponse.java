package phone.nikolas.com.rxjavasample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pleret on 3/7/2017.
 */

public class EmailListResponse {
    @SerializedName("email")
    @Expose
    private List<String> email = new ArrayList<String>();

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }
}
