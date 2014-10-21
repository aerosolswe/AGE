package com.theodore.aero.graphics.mesh;

import com.theodore.aero.core.Aero;
import com.theodore.aero.graphics.mesh.meshLoading.IndexedModel;
import com.theodore.aero.graphics.mesh.meshLoading.OBJModel;
import com.theodore.aero.resourceManagement.MeshResource;

import java.io.File;
import java.util.HashMap;

public class Mesh {

    private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
    private MeshResource resource;
    private String name;

    public Mesh(String name, File file) {
        this.name = name;
        MeshResource oldResource = loadedModels.get(name);

        if (oldResource != null) {
            resource = oldResource;
            resource.addReference();
        } else {
            resource = loadMesh(file);
            loadedModels.put(name, resource);
        }
    }

    public Mesh(String name, IndexedModel model) {
        this.name = name;

        MeshResource oldResource = loadedModels.get(name);

        if (oldResource != null) {
            resource = oldResource;
            resource.addReference();
        } else {
            resource = new MeshResource(model);
            loadedModels.put(name, resource);
        }
    }

    public Mesh(String name) {
        this.name = name;

        MeshResource oldResource = loadedModels.get(name);

        if (oldResource != null) {
            resource = oldResource;
            resource.addReference();
        } else {
            System.err.println("Couldnt find the mesh with specified name '" + name + "', using default cube!");
            resource = loadedModels.get("cube");
        }
    }

    @Override
    protected void finalize() {
        if (resource.removeReference() && !name.isEmpty()) {
            loadedModels.remove(name);
        }
    }

    public void draw(int mode) {
        resource.draw(mode);
    }

    private static MeshResource loadMesh(File file) {
        String[] splitArray = file.getName().split("\\.");
        String ext = splitArray[splitArray.length - 1];

        if (!ext.equals("obj")) {
            System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
            new Exception().printStackTrace();
            System.exit(1);
        }

        OBJModel test = new OBJModel(file);
        IndexedModel model = test.toIndexedModel();

        return new MeshResource(model);
    }

    public static void generateBasicMesh() {
        loadedModels.put("cube", new MeshResource(Primitives.cube()));
        loadedModels.put("plane", new MeshResource(Primitives.plane()));
        loadedModels.put("rectangle", new MeshResource(Primitives.rectangle()));
        loadedModels.put("dome", loadMesh(Aero.files.internal("default/models/dome.obj")));
        loadedModels.put("sphere", loadMesh(Aero.files.internal("default/models/sphere.obj")));
    }

}
