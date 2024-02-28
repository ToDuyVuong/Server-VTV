package hcmute.kltn.vtv.service.location;

import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.extra.RegionLevel;

public interface IRegionMatcherService {
    boolean matches(Ward customerWard, Ward shopWard, RegionLevel level);
}
