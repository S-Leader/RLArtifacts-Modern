package artifacts.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RegistrySupplier<T> implements Supplier<T> {

    private final RegistryObject<T> supplier;

    private RegistrySupplier(RegistryObject<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> RegistrySupplier<T> of(RegistryObject<T> supplier) {
        return new RegistrySupplier<>(supplier);
    }

    @Override
    public T get() {
        return supplier.get();
    }

    public ResourceLocation getId() {
        return supplier.getId();
    }

    public RegistryObject<T> getRegistryObject() {
        return supplier;
    }
}
