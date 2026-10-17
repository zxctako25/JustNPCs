package org.zxc_tako25.justnpcs.npc;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public final class NpcHologram {
    public static final int MAX_LINES = 32;
    public static final double DEFAULT_OFFSET = 0.25;
    private final List<String> lines;
    private double offset;

    public NpcHologram(List<String> lines, double offset) {
        this.lines = new ArrayList<>(lines == null ? Collections.emptyList() : lines);
        this.offset = normalizeOffset(offset);
    }

    public static NpcHologram empty() {
        return new NpcHologram(Collections.emptyList(), DEFAULT_OFFSET);
    }

    public void addLine(String line) {
        if (line == null)
            return;
        if (lines.size() >= MAX_LINES)
            return;
        lines.add(line);
    }

    public boolean removeLine(int index) {
        if (index < 0 || index >= lines.size())
            return false;

        lines.remove(index);
        return true;
    }

    public boolean setLine(int index, String line) {
        if (index < 0 || index >= lines.size())
            return false;

        lines.set(index, line);
        return true;
    }

    public void clear() {
        lines.clear();
    }

    public void setOffset(double offset) {
        this.offset = normalizeOffset(offset);
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public int size() {
        return lines.size();
    }

    public List<String> snapshot() {
        return Collections.unmodifiableList(lines);
    }

    private static double normalizeOffset(double offset) {
        if (offset < 0.0 || offset > 1.0)
            return DEFAULT_OFFSET;
        return offset;
    }
}
