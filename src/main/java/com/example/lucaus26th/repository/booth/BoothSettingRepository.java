package com.example.lucaus26th.repository.booth;

import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.domain.booth.BoothSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothSettingRepository extends JpaRepository<BoothSetting,Long> {
}
