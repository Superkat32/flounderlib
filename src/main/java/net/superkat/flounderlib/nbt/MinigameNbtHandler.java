package net.superkat.flounderlib.nbt;

public class MinigameNbtHandler {

//    public static void readNbt(Object minigame, NbtCompound nbt) throws IllegalAccessException {
//        Class<?> clazz = minigame.getClass();
//        for (Field field : clazz.getFields()) {
//            MinigameNbt nbtAnnotation = field.getAnnotation(MinigameNbt.class);
//            if(nbtAnnotation != null) {
//                String name = field.getName();
//                if(!nbtAnnotation.name().isEmpty() && !nbtAnnotation.name().isBlank()) {
//                    name = nbtAnnotation.name();
//                }
//
//                Class<?> type = field.getType();
//                for (Class<?> nbtableClass : FlounderNbtApi.nbtSerializers.keySet()) {
//                    if (type.isAssignableFrom(nbtableClass)) {
//                        NbtSerializer<?> serializer = FlounderNbtApi.nbtSerializers.get(nbtableClass);
//                        Object value = serializer.readNbt(nbt, name);
//                        field.set(minigame, value);
//                    }
//                }
//
//            }
//        }
//    }
//
//    public static NbtCompound writeNbt(Object minigame, NbtCompound nbt) throws IllegalAccessException {
//        Class<?> clazz = minigame.getClass();
//        for (Field field : clazz.getFields()) {
//            MinigameNbt nbtAnnotation = field.getAnnotation(MinigameNbt.class);
//            if(nbtAnnotation != null) {
//                String name = field.getName();
//                if(!nbtAnnotation.name().isEmpty() && !nbtAnnotation.name().isBlank()) {
//                    name = nbtAnnotation.name();
//                }
//
//                Class<?> type = field.getType();
//                for (Class<?> nbtableClass : FlounderNbtApi.nbtSerializers.keySet()) {
//                    if (type.isAssignableFrom(nbtableClass)) {
//                        NbtSerializer<?> serializer = FlounderNbtApi.nbtSerializers.get(nbtableClass);
//                        Object value = field.get(minigame);
//                        serializer.writeNbt(nbt, name, value);
//                    }
//                }
//
//            }
//        }
//        return nbt;
//    }

}
