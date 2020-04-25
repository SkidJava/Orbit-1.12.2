package client.manager.managers;

import client.event.EventTarget;
import client.event.events.misc.EventChangeOption;
import client.event.events.misc.EventKeyboard;
import client.manager.AbstManager;
import client.module.Empty;
import client.module.Module;
import client.utils.progressbar.ProgressBar;
import client.utils.progressbar.ProgressBarList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModuleManager extends AbstManager {

    private Module empty = new Empty();
    private ArrayList<Module> mods = new ArrayList();

    @Override
    public void asyncLoad() {
        Reflections reflect = new Reflections(Module.class);
        Set modules = reflect.getSubTypesOf(Module.class);
        float progress = 0;
        for (Object obj : modules) {
            Class objClass = (Class) obj;
            try {
                progress += 100.0f / modules.size();
                Module mod = (Module) objClass.newInstance();
                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
                ProgressBar progressBar = new ProgressBar(scaledresolution.getScaledWidth() / 2.0f, scaledresolution.getScaledHeight() / 2.0f, 100, 10, progress, "Loading Module / " + mod.getName(), -1);
                ProgressBarList.addProgress(0, progressBar);

                if (objClass.getSuperclass() != Module.class || mod instanceof Empty) {
                    continue;
                }

                mods.add(mod);

                Reflections reflect1 = new Reflections(mod.getClass());
                Set components = reflect1.getSubTypesOf(mod.getClass());
                ProgressBarList.removeProgress(1);
                float progress1 = 0;
                for (Object obj1 : components) {
                    Class objClass1 = (Class) obj1;
                    Module mod1 = (Module) objClass1.newInstance();
                    if (objClass1.getSuperclass() == mod.getClass()) {
                        mod.addComponent(mod, mod1);
                        progress1 += 100.0f / components.size();
                        ProgressBar progressBar1 = new ProgressBar(scaledresolution.getScaledWidth() / 2, scaledresolution.getScaledHeight() / 2 + 30, 100, 10, progress1, "Loading Components " + mod.getComponents().size() + " / " + components.size(), -1);
                        ProgressBarList.addProgress(1, progressBar1);
                    }
                }

            } catch (Exception e) {
            }
        }

        mods.sort((o1, o2) -> o2.getName().length() - o1.getName().length());
        managers.eventManager.register(this);
    }

    private void addModule(Module m) {
        this.mods.add(m);
    }

    public ArrayList<Module> getMods() {
        return this.mods;
    }

    public ArrayList<Module> getToggledMods() {
        ArrayList<Module> mods = new ArrayList();
        for (Module m : getMods()) {
            if (m.isEnabled()) {
                mods.add(m);
            }
        }
        return mods;
    }

    public ArrayList<Module> getVisibleMods() {
        ArrayList<Module> mods = new ArrayList();
        for (Module m : getMods()) {
            if (m.isEnabled() && m.isVisible()) {
                mods.add(m);
            }
        }
        return mods;
    }

    public List<Module> getModulesInCategory(Module.Category category) {
        List<Module> list = new ArrayList<>();
        for (Module mod : getMods()) {
            if (mod.getCategory() == category) list.add(mod);
        }
        return list;
    }

    public ArrayList<Module> getVisibleComponents(Module module) {
        ArrayList<Module> components = new ArrayList<>();
        for (Module m : module.getComponents()) {
            if (m.isVisible()) {
                components.add(m);
            }
        }

        return components;
    }

    public Module getModule(String name) {
        for (Module m : this.mods) {
            if (m.getName().equalsIgnoreCase(name))
                return m;
        }
        return empty;
    }

    public Module getModule(Module mods) {
        for (Module m : this.mods) {
            if (m == mods)
                return m;
        }
        return empty;
    }

    public Module getModule(Class<? extends Module> mods) {
        for (Module m : this.mods) {
            if (m.getClass() == mods)
                return m;
        }
        return empty;
    }

    @EventTarget
    public void onKey(EventKeyboard e) {
        for (Module m : this.mods)
            if (m.getKeyCode() == e.getKey())
                m.toggle();
    }

    @EventTarget
    public void onOptionChange(EventChangeOption event) {
        for (Module module : event.getModule().getComponents()) {
            if(module.getName().equals(event.getCurrentOption()))
                module.onEnable();
            else if(module.getName().equals(event.getPreviousOption()))
                module.onDisable();
        }
    }
}
