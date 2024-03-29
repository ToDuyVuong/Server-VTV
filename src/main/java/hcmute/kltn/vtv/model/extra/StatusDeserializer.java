package hcmute.kltn.vtv.model.extra;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import hcmute.kltn.vtv.util.exception.BadRequestException;

import java.io.IOException;

public class StatusDeserializer extends StdDeserializer<Status> {

    public StatusDeserializer() {
        this(null);
    }

    public StatusDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Status deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String statusValue = jsonParser.getText();
        Status status = Status.fromValue(statusValue);

        if (status == null) {
            throw new BadRequestException("Trạng thái không hợp lệ!");
        }

        return status;
    }
}