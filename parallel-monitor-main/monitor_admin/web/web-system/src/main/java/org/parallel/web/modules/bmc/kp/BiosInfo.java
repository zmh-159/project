package org.parallel.web.modules.bmc.kp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.parallel.web.modules.bmc.Kp;
import org.parallel.web.modules.bmc.Rtc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public class BiosInfo {
    @Autowired
    Kp kp;

    @Autowired
    private Rtc rtc;

    public JSONObject getBios(Long nodeId) {
        JSONObject res = new JSONObject();

        String url = kp.getBiosUrl(kp.getIp(nodeId));
        ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);

        JSONObject info = JSON.parseObject(response.getBody());

        JSONObject bios = new JSONObject();
        JSONObject attributes = info.getJSONObject("Attributes");

        bios.put("name", info.get("Name"));
        bios.put("id", info.get("Id"));
        bios.put("attributeRegistry", info.get("AttributeRegistry"));
        bios.put("quickBoot", attributes.get("QuickBoot"));
        bios.put("quietBoot", attributes.get("QuietBoot"));
        bios.put("pxeBootToLanUEFI", attributes.get("PXEBootToLanUEFI"));
        bios.put("pxeBootToLanLegacy", attributes.get("PXEBootToLanLegacy"));
        bios.put("pxeTimeoutRetryControl", attributes.get("PxeTimeoutRetryControl"));
        bios.put("bootTypeOrder0", attributes.get("BootTypeOrder0"));
        bios.put("bootTypeOrder1", attributes.get("BootTypeOrder1"));
        bios.put("bootTypeOrder2", attributes.get("BootTypeOrder2"));
        bios.put("bootTypeOrder3", attributes.get("BootTypeOrder3"));
        bios.put("customPowerPolicy", attributes.get("CustomPowerPolicy"));
        bios.put("turboMode", attributes.get("TurboMode"));
        bios.put("processorHyperThreadingDisable", attributes.get("ProcessorHyperThreadingDisable"));
        bios.put("processorEISTEnable", attributes.get("ProcessorEISTEnable"));
        bios.put("powerSaving", attributes.get("PowerSaving"));
        bios.put("systemCpuUsage", attributes.get("SystemCpuUsage"));
        bios.put("pstateDomain", attributes.get("PStateDomain"));
        bios.put("processorAutonomousCstateEnable", attributes.get("ProcessorAutonomousCstateEnable"));
        bios.put("processorC1eEnable", attributes.get("ProcessorC1eEnable"));
        bios.put("c6Enable", attributes.get("C6Enable"));
        bios.put("numaEn", attributes.get("NumaEn"));
        bios.put("pcieSRIOVSupport", attributes.get("PCIeSRIOVSupport"));
        bios.put("pcieARISupport", attributes.get("PCIeARISupport"));



        res.put(res.size() + "", bios);

        return res;
    }
}
