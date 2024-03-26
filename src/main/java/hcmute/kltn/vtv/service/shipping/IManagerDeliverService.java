package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.shipping.request.DeliverRequest;
import hcmute.kltn.vtv.model.data.shipping.request.UpdateDeliverWorkRequest;
import hcmute.kltn.vtv.model.data.shipping.request.UpdateStatusDeliverRequest;
import hcmute.kltn.vtv.model.data.shipping.response.DeliverResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ListDeliverResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.TypeWork;
import org.springframework.transaction.annotation.Transactional;

public interface IManagerDeliverService {
    @Transactional
    DeliverResponse addNewDeliver(DeliverRequest request);

    @Transactional
    DeliverResponse updateDeliverWork(UpdateDeliverWorkRequest request);

    @Transactional
    DeliverResponse updateStatusDeliver(UpdateStatusDeliverRequest request);

    ListDeliverResponse getListDeliverByStatus (Status status);


    ListDeliverResponse getListDeliverByStatusAndTypeWork(Status status, TypeWork typeWork);
}
