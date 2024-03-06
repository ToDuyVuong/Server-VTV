package hcmute.kltn.vtv.repository.location;

import hcmute.kltn.vtv.model.entity.location.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
    // Additional custom queries can be added here if needed
    Optional<List<Province>> findAllByAdministrativeRegion_AdministrativeRegionId(Integer administrativeRegionId);

    Optional<List<Province>> findAllByAdministrativeUnit_AdministrativeUnitId(Integer administrativeUnitId);

    Optional<Province> findByProvinceCode(String provinceCode);


    @Query(value = "SELECT p.* " +
            "FROM wards w " +
            "JOIN districts d ON w.district_code = d.code " +
            "JOIN provinces p ON d.province_code = p.code " +
            "WHERE w.code = :wardCode",
            nativeQuery = true)
    Optional<Province> findProvinceByWardCode(String wardCode);


}
