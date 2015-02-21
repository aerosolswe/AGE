package com.theodore.aero.input;

import com.theodore.aero.core.Aero;
import com.theodore.aero.core.Input;
import com.theodore.aero.math.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InputManager {

    private HashMap<Integer, Mapping> mappings = new HashMap<Integer, Mapping>();

    private KeyCallback keyCallback;
    private MouseButtonCallback mouseButtonCallback;
    private ScrollCallback scrollCallback;

    public InputManager() {

        keyCallback = new KeyCallback() {

            @Override
            public void keyCallback(long window, int key, int scancode, int action, int mods) {
                for (Map.Entry<Integer, Mapping> entry : mappings.entrySet()) {
                    Mapping mapping = entry.getValue();

                    if (mapping != null) {
                        for (int i = 0; i < mapping.getKeys().size(); i++) {
                            if (mapping.getKey(i) == key) {
                                if (action == Input.Actions.PRESS) {
                                    mapping.setPressed(true);
                                }

                                if (action == Input.Actions.RELEASE) {
                                    mapping.setReleased(true);
                                }

                                if (action == Input.Actions.REPEAT) {
                                    mapping.setRepeated(true);
                                } else {
                                    mapping.setRepeated(false);
                                }
                            }
                        }
                    }
                }
            }
        };

        mouseButtonCallback = new MouseButtonCallback() {
            @Override
            public void mouseCallback(long window, int button, int action, int mods) {
                for (Map.Entry<Integer, Mapping> entry : mappings.entrySet()) {
                    Mapping mapping = entry.getValue();

                    if (mapping != null) {
                        for (int i = 0; i < mapping.getButtons().size(); i++) {
                            if (mapping.getButton(i) == button) {
                                if (action == Input.Actions.PRESS) {
                                    mapping.setPressed(true);
                                }

                                if (action == Input.Actions.RELEASE) {
                                    mapping.setReleased(true);
                                }

                                if (action == Input.Actions.REPEAT) {
                                    mapping.setRepeated(true);
                                } else {
                                    mapping.setRepeated(false);
                                }
                            }
                        }
                    }
                }
            }
        };

        scrollCallback = new ScrollCallback() {
            @Override
            public void scrollCallback(long window, double xoffset, double yoffset) {
                for (Map.Entry<Integer, Mapping> entry : mappings.entrySet()) {
                    Mapping mapping = entry.getValue();

                    if (mapping != null) {
                        if (mapping.checkMwheelup) {
                            if (yoffset >= 1) {
                                mapping.setMwheelup(true);
                            }
                        }

                        if (mapping.checkMwheeldown) {
                            if (yoffset <= -1) {
                                mapping.setMwheeldown(true);
                            }
                        }
                    }
                }
            }
        };

        Aero.input.setKeyCallback(keyCallback);
        Aero.input.setMouseButtonCallback(mouseButtonCallback);
        Aero.input.setScrollCallback(scrollCallback);
    }

    public void update() {
        if (mappings.size() > 0) {
            for (Map.Entry<Integer, Mapping> entry : mappings.entrySet()) {
                Mapping mapping = entry.getValue();
                if (mapping != null) {
                    mapping.setPressed(false);
                    mapping.setReleased(false);
                    mapping.setMwheeldown(false);
                    mapping.setMwheelup(false);
                }
            }
        }
    }

    public void addKeyMap(String name, int key) {
        Mapping mapping = null;

        for (Map.Entry<Integer, Mapping> entry : mappings.entrySet()) {
            mapping = entry.getValue();
            if (mapping.getName().equals(name)) {
                mapping.addKey(key);
            } else {
                mapping = null;
            }
        }

        if (mapping == null) {
            mapping = new Mapping(name, key, true);
            mappings.put(key, mapping);
        }
    }

    public void addMouseMap(String name, int button) {
        Mapping mapping = null;

        for (Map.Entry<Integer, Mapping> entry : mappings.entrySet()) {
            mapping = entry.getValue();
            if (mapping.getName().equals(name)) {
                mapping.addButton(button);
            } else {
                mapping = null;
            }
        }

        if (mapping == null) {
            mapping = new Mapping(name, button, false);
            mappings.put(button, mapping);
        }
    }

    public void addScrollMap(String name, boolean up, boolean down) {
        Mapping mapping = null;

        for (Map.Entry<Integer, Mapping> entry : mappings.entrySet()) {
            mapping = entry.getValue();
            if (mapping.getName().equals(name)) {
                mapping.checkMwheeldown = down;
                mapping.checkMwheelup = up;
            } else {
                mapping = null;
            }
        }

        if (mapping == null) {
            mapping = new Mapping(name);
            mapping.checkMwheeldown = down;
            mapping.checkMwheelup = up;
            mappings.put((int) MathUtils.random(900, 1000), mapping);
        }
    }

    public Mapping getMapping(String name) {
        Mapping mapping = null;

        for (Map.Entry<Integer, Mapping> entry : mappings.entrySet()) {
            mapping = entry.getValue();
            if (mapping != null) {
                if (mapping.getName().equals(name)) {
                    return mapping;
                }
            }
        }

        return mapping;
    }

    public void dispose() {
        keyCallback.release();
        mouseButtonCallback.release();
        scrollCallback.release();
    }
}
