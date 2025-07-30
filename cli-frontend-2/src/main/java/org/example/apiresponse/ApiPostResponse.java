package org.example.apiresponse;

import org.example.dto.PostDTO;
import java.util.List;

public class ApiPostResponse {
    private boolean success;
    private List<PostDTO> data;

    public boolean isSucces() {
        return success;
    }

    public List<PostDTO> getData() {
        return data;
    }
}
