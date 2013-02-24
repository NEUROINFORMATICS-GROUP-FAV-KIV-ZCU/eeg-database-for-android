package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Root(name = "scenarios")
public class ScenarioDataList {

    @ElementList(inline = true, required = false)
    private List<ScenarioData> scenarios;

    public List<ScenarioData> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<ScenarioData> scenarios) {
        this.scenarios = scenarios;
    }
}
