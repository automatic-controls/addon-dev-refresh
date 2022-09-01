package aces.webctrl.dev;
import com.controlj.green.addonsupport.*;
import javax.servlet.*;
import java.nio.file.*;
public class Initializer implements ServletContextListener {
  public volatile AddOnInfo info;
  private volatile String name;
  private volatile Path addons;
  private volatile Thread thread;
  private volatile boolean go = true;
  @Override public void contextInitialized(ServletContextEvent sce){
    try{
      info = AddOnInfo.getAddOnInfo();
      name = info.getName();
      addons = info.getPrivateDir().toPath().getParent().getParent().getParent().getParent().getParent().resolve("addons").normalize();
      thread = new Thread(){
        @Override public void run(){
          String addonName;
          Path addon;
          while (go){
            try{
              try(
                DirectoryStream<Path> stream = Files.newDirectoryStream(addons);
              ){
                for (Path p:stream){
                  try{
                    addonName = p.getFileName().toString();
                    if (addonName.endsWith(".update")){
                      addonName = addonName.substring(0,addonName.length()-7);
                      if (!addonName.equals(name)){
                        addon = addons.resolve(addonName+".addon");
                        if (!Files.exists(addon) || HelperAPI.disableAddon(addonName)){
                          try{
                            Files.move(p,addon,StandardCopyOption.REPLACE_EXISTING);
                          }catch(Throwable t){
                            continue;
                          }
                          if (Files.exists(addon)){
                            HelperAPI.enableAddon(addonName);
                            HelperAPI.activateWebOperatorProvider(addon);
                          }
                        }
                      }
                    }
                  }catch(Throwable t){
                    t.printStackTrace();
                  }
                }
              }
              Thread.sleep(3000);
            }catch(InterruptedException e){}catch(Throwable t){
              t.printStackTrace();
            }
          }
        }
      };
      thread.start();
    }catch(Throwable t){
      t.printStackTrace();
    }
  }
  @Override public void contextDestroyed(ServletContextEvent sce){
    try{
      go = false;
      thread.interrupt();
      thread.join();
    }catch(Throwable t){
      t.printStackTrace();
    }
  }
}