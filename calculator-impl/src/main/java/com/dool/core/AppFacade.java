package com.dool.core;

import com.dool.BasicCalculator;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppFacade {
    private Map<String,String> data = new HashMap<>();
    private BasicCalculator calculator = new BasicCalculator();
    private final Logger LOG = LogManager.getLogger(getClass());

    public void run(){

        data.put("+", "+");
        data.put("-", "-");
        data.put("/", "/");
        data.put("*", "*");
        data.put("^", "^");
        data.put("%", "%");

        plugin();

        Scanner in = new Scanner(System.in);
//         double a, b;

        while(true) {
//            System.out.println("\nWybierz operację do wykonania:");
//            for (Map.Entry<String, String> pair : data.entrySet()) {
//                System.out.println(pair.getKey());
//            }
//            String choice = in.next();
//
//            System.out.println("Podaj pierwszą liczbę");
//            a = in.nextDouble();
//            System.out.println("Podaj druga liczbę");
//            b = in.nextDouble();
//            LOG.info("a = {}", a);
//            LOG.info("b = {}", b);
//            try {
//                System.out.println(calculator.result(choice, a, b));
//            } catch (IllegalArgumentException e) {
//                LOG.error(e);
//            }

            System.out.print("\nNaciśnij w, żeby wyjść\nPodaj wyrażenie: ");
            String equation = in.nextLine();
            if (equation.equals("w")) {
                System.exit(0);
            } else {
                try {
                    Rpn rpn = new Rpn(equation, this.calculator);

                    System.out.println("Wyrażenie postfiksowe: " + rpn);

                    double result = rpn.calRPN();
                    System.out.println("Wynik: " + result);
                } catch (IllegalArgumentException e) {
                    LOG.error(e);
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    private void plugin(){
        File [] fileList = loadJar();

        for(File f : fileList){
            ArrayList<String> names = loadClass(f);
            for(String name : names){
                URL[] classLoaderUrls;
                try {
                    classLoaderUrls = new URL[]{new URL("file:///"+System.getProperty("user.dir")+"/plugins/"+f.getName())};
                    URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls,Thread.currentThread().getContextClassLoader());
                    Class<?> cl = urlClassLoader.loadClass(name);
                    Constructor<?> constructor = cl.getConstructor();

                    Method ic = (Method) constructor.newInstance();

                    data.put(ic.sign(), ic.sign());
                    calculator.add(ic);

                } catch (MalformedURLException | InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                    LOG.error(e);
                    e.printStackTrace();
                }
            }
        }
    }

    private File[] loadJar(){
        File folder = new File(System.getProperty("user.dir")+"/plugins/");

        return folder.listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".jar"));
    }

    private ArrayList<String> loadClass(File file) {
        JarFile jfile;

        try {
            jfile = new JarFile(file);
            Enumeration<JarEntry> entries = jfile.entries();
            String name;
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
            LOG.error(e);
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
