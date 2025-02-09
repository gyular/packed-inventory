package dev.kir.packedinventory.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public abstract class InvokableKeyBinding extends KeyBinding {
    protected InvokableKeyBinding(String translationKey, int code, String category) {
        this(translationKey, InputUtil.Type.KEYSYM, code, category);
    }

    protected InvokableKeyBinding(String translationKey, InputUtil.Type type, int code, String category) {
        super(translationKey, type, code, category);
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            while (this.wasPressed()) {
                this.invoke();
            }
        });
    }

    public static KeyBinding create(String translationKey, int code, String category, Runnable action) {
        return InvokableKeyBinding.create(translationKey, InputUtil.Type.KEYSYM, code, category, action);
    }

    public static KeyBinding create(String translationKey, InputUtil.Type type, int code, String category, Runnable action) {
        return new InvokableKeyBinding(translationKey, type, code, category) {
            @Override
            protected void invoke() {
                action.run();
            }
        };
    }

    protected abstract void invoke();
}
