package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RequiredArgsConstructor
public class ProductAttributeRequest {

    private String name;

    private String value;


    public void validate() {
        if (this.name == null || this.name.isEmpty()) {
            throw new BadRequestException("Tên thuộc tính không được để trống!");
        }
        if (this.value == null || this.value.isEmpty()) {
            throw new BadRequestException("Giá trị thuộc tính không được để trống!");
        }
        trim();
    }


    private void trim() {
        this.name = this.name.trim();
        this.value = this.value.trim();
    }
}
