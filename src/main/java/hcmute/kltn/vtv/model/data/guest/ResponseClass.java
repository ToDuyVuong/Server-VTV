package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor

public class ResponseClass extends ResponseAbstract {

    public static ResponseClass responseClass(String message, String status) {
        ResponseClass responseClass = new ResponseClass();
        responseClass.setMessage(message);
        responseClass.setStatus(status);
        responseClass.setCode(200);

        return responseClass;
    }
}
