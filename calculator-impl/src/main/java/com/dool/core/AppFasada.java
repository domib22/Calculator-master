package com.dool.core;

import com.dool.BasicCalculator;
import com.dool.cal.Calculator;
import com.dool.met.Method;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AppFasada {
    private Map<String,String> data = new HashMap<>();
    private File[] listOfFiles;
    private BasicCalculator calculator = new BasicCalculator();
    public void run(){


        data.put("+", "+");
        data.put("-", "-");
        data.put("/", "/");
        data.put("*", "*");
        Scanner in = new Scanner(System.in);

        double a, b;
        plugin();

        System.out.println("Podaj pierwszą liczbę");
        a = in.nextDouble();
        System.out.println("Podaj druga liczbę");
        b = in.nextDouble();

        System.out.println("Wybierz operację do wykonania:");


        for(Map.Entry<String,String> pair: data.entrySet()){
            System.out.println(pair.getKey());
        }
        String choice = in.next();
        calculator.result(choice,a,b);

    }
    private void plugin(){
        File [] listOfFiles=loadFromFile();
        for(File file : listOfFiles){
            ArrayList<String> names=getNamesClass(file);
            for(String name : names){
                URL[] classLoaderUrls = new URL[0];
                try {
                    classLoaderUrls = new URL[]{new URL("file:///"+System.getProperty("user.dir")+"/plugins/"+file.getName())};
                    URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls,Thread.currentThread().getContextClassLoader());
                    Class<?> cl = urlClassLoader.loadClass(name);
                    Constructor<?> constructor =cl.getConstructor();
                    Method ic = (Method) constructor.newInstance();

                    data.put(ic.sign(), ic.sign());
                    calculator.add(ic);

                } catch (MalformedURLException | InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


        }
        }

    private File[] loadFromFile(){
        File folder = new File(System.getProperty("user.dir")+"/plugins/");
        return folder.listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".jar"));
    }

    private ArrayList<String> getNamesClass(File file) {
        JarFile jf = null;

        try {
            jf = new JarFile(file);
            Enumeration<JarEntry> entries = jf.entries();
            String name = "";
            ArrayList<String> names = new ArrayList<>();
            while (entries.hasMoreElements()) {
                name = entries.nextElement().getName();
                if (name.endsWith(".class")) {
                    name = name.substring(0, name.length() - 6);
                    names.add(name);
                }
            }
            return names;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }
}
