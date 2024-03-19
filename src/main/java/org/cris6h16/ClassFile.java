package org.cris6h16;

import java.io.File;
import java.io.IOException;

public class ClassFile {
    public static void main(String[] args) throws IOException {
            File file = new File("javaFile123.txt");
        try {
            if (file.createNewFile()) {
                System.out.println("New File is created!");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        File f=new File("/home/cristian/Desktop");
        String filenames[]=f.list();
        for(String filename:filenames){
            System.out.println(filename);
        }

        System.out.println(
                file.exists()+" \n"+
                file.getAbsolutePath()+" \n"+
                file.getName()+" \n"+
                file.getParent()+" \n"+
                file.getPath()+" \n"+
                file.canRead()+" \n"+
                file.canWrite()+" \n"+
                file.length()+" \n"+
                file.lastModified()+" \n"+
                file.delete()+" \n"+
                file.mkdir()+" \n"+
                file.renameTo(new File("javaFile1234.txt"))+" \n"+
                file.isDirectory()+" \n"+
                file.isFile()+" \n"+
                file.isHidden()+" \n"+
                file.canExecute()+" \n"+
                file.getFreeSpace()+" \n"+
                file.getTotalSpace()+" \n"+
                file.getUsableSpace()+" \n"+
                file.toPath()+" \n"+
                file.toURI()+" \n"+ //file:/home/cristian/personal/intellij/IdeaProjects/Files/javaFile123.txt
                file.hashCode()+" \n"+
                file.getClass()+" \n"+
                file.toString()+" \n"+
                file.equals(new File("javaFile1234.txt"))+" \n"+
                file.compareTo(new File("javaFile1234.txt"))+" \n"
        );

        String[] filesInDesktop = f.list();
        if (filesInDesktop != null) {
            for (String fileInDesktop : filesInDesktop) {
                System.out.println(fileInDesktop);
            }
        }
    }
}
