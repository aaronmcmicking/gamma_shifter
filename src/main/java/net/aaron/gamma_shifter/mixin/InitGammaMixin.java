package net.aaron.gamma_shifter.mixin;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import net.aaron.gamma_shifter.GammaShifter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;


@Mixin(GameOptions.class)
public class InitGammaMixin {
    MinecraftClient mc = MinecraftClient.getInstance();
    private Double found_gamma = 1.0;
    private final String TEMP_FILE_NAME = "temp_gamma_shifter.txt";

    private static final Splitter COLON_SPLITTER = Splitter.on(':').limit(2);

//    @Inject(method = "load", at = @At("HEAD"), cancellable = false)
//    public void retrieveGammaInjection(CallbackInfo ci){
//        GameOptions options = (GameOptions) (Object) this;
//        if(!options.getOptionsFile().exists()){
//            return;
//        }
//
//        NbtCompound nbtCompound = new NbtCompound();
//        try (BufferedReader bufferedReader = Files.newReader(options.getOptionsFile(), Charsets.UTF_8);){
//            bufferedReader.lines().forEach(line -> {
//                try {
//                    Iterator<String> iterator = COLON_SPLITTER.split((CharSequence)line).iterator();
//                    nbtCompound.putString(iterator.next(), iterator.next());
//                } catch (Exception exception) {
//                    GammaShifter.LOGGER.warn("[GammaShifter] Skipping bad option: {} (encountered while retrieving gamma)", line);
//                }
//            });
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        if(nbtCompound.contains("gamma")){
//            found_gamma = Double.parseDouble(nbtCompound.getString("gamma"));
//            GammaShifter.LOGGER.info("[GammaShifter] Found existing gamma setting of " + found_gamma);
//        }else{
//            GammaShifter.LOGGER.info("[GammaShifter] Couldn't find an existing gamma setting... is the key value incorrect?");
//        }
//        try {
//            writeFoundGammaToFile();
//        }catch(IOException e){
//            GammaShifter.LOGGER.info("[GammaShifter] writeFoundGammaToFile threw IOException");
//        }
//    }

//    @Inject(method = "load", at = @At("RETURN"), cancellable = false)
//    public void initializeGammaInjection(CallbackInfo ci){
//        try{
//            readFoundGammaFromFile();
//        }catch(IOException e){
//            GammaShifter.LOGGER.info("[GammaShifter] LoadFoundGammaFromFile threw IOException");
//            this.found_gamma = 1.0;
//        }
//        mc.options.getGamma().setValue(1.0);
//    }

    @Inject(method = "load", at = @At("HEAD"), cancellable = false)
    public void injectTest(CallbackInfo ci){
        GameOptions options = (GameOptions) (Object) this;
//        GammaShifter.LOGGER.info("Injected into GameOptions.load() successfully (optionsFile = " + options.getOptionsFile().toString());

        NbtCompound nbtCompound = new NbtCompound();
        try (BufferedReader bufferedReader = Files.newReader(options.getOptionsFile(), Charsets.UTF_8);){
            bufferedReader.lines().forEach(line -> {
                try {
                    Iterator<String> iterator = COLON_SPLITTER.split((CharSequence)line).iterator();
                    nbtCompound.putString(iterator.next(), iterator.next());
                } catch (Exception exception) {
                    GammaShifter.LOGGER.warn("[GammaShifter] Skipping bad option: {} (encountered while retrieving gamma)", line);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(nbtCompound.contains("gamma")){
            found_gamma = Double.parseDouble(nbtCompound.getString("gamma"));
            GammaShifter.LOGGER.info("[GammaShifter] Found existing gamma setting of " + found_gamma);
        }else{
            GammaShifter.LOGGER.info("[GammaShifter] Couldn't find an existing gamma setting... is the key value incorrect?");
        }
    }

    @Inject(method = "load", at = @At("RETURN"), cancellable = false)
    public void persistenceTest(CallbackInfo ci){
//        GammaShifter.LOGGER.info("[GammaShifter] Injected into end of GameOptions.load(): found_gamma = " + found_gamma);
        GameOptions options = (GameOptions) (Object) this;
        options.getGamma().setValue(found_gamma);
    }

    public void writeFoundGammaToFile() throws IOException {
        File f = new File(TEMP_FILE_NAME);
        OutputStream s = null;
        try {
            s = new FileOutputStream(f);
        }catch(FileNotFoundException e){
            GammaShifter.LOGGER.info("[GammaShifter] writeFoundGammaToFile threw FileNotFoundException");
            return;
        }
        String str = this.found_gamma.toString();
        for(int i = 0; i < str.length(); i++){
                s.write(str.charAt(i));
        }
        s.close();
    }

    public void readFoundGammaFromFile() throws IOException{
        File f = new File(TEMP_FILE_NAME);
        InputStream s = null;
        try{
            s = new FileInputStream(f);
        }catch(FileNotFoundException e){
            GammaShifter.LOGGER.info("[GammaShifter] LoadFoundGammaFromFile threw FileNotFoundException");
        }

        byte[] bytes = new byte[10];
        try{
            s.read(bytes);
        }catch(NullPointerException e){
            GammaShifter.LOGGER.info("[GammaShifter] LoadFoundGammaFromFile threw NullPointerException");
        }
        found_gamma = Double.parseDouble(new String(bytes, StandardCharsets.UTF_8));
    }
}
