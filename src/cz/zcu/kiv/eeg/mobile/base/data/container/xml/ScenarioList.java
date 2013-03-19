package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

import java.util.List;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
@Root(name = "scenarios")
public class ScenarioList {

    @ElementList(inline = true, required = false)
    private List<Scenario> scenarios;

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public boolean isAvailable(){
        return scenarios != null;
    }
}
