package com.weihq.opentype4j;

import com.weihq.opentype4j.table.CmapTable;
import com.weihq.opentype4j.util.StringUtils;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.HashMap;
import java.util.Map;

/**
 * An OpenType font file
 *
 * @author Jkanon
 * @date 2019/06/06
 **/
public class Font extends AbstractParser<Font> {

    private int numGlyphs;

    private boolean isCIDFont = false;

    private int defaultWidthX;

    private int nominalWidthX;

    private int unitsPerEm;

    private int ascender;

    private int descender;

    private String outlinesFormat;

    private GlyphNames glyphNames;

    private GlyphDataList glyphs;

    private Map<String, Object> tables = new HashMap<>();

    @Override
    protected void parse() {
        this.numGlyphs = fetchIntValue("numGlyphs");
        this.isCIDFont = fetchBooleanValue("isCIDFont");
        this.defaultWidthX = fetchIntValue("defaultWidthX");
        this.nominalWidthX = fetchIntValue("nominalWidthX");
        this.unitsPerEm = fetchIntValue("unitsPerEm");
        this.ascender = fetchIntValue("ascender");
        this.descender = fetchIntValue("descender");
        this.outlinesFormat = fetch("outlinesFormat");
        this.glyphNames = new GlyphNames().parse(fetch("glyphNames"));
        this.glyphs = new GlyphDataList().parse(fetch("glyphs"));
        ScriptObjectMirror tables = fetch("tables");

        this.tables.put("cmap", new CmapTable().parse((ScriptObjectMirror) tables.get("cmap")));
    }

    public CmapTable getCamp() {
        return (CmapTable) tables.get("cmap");
    }

    /**
     * Check if the font has a glyph for the given character.
     * @param charcode
     * @return
     */
    public boolean hasChar(int charcode) {
        return (boolean) scriptObjectMirror.callMember("hasChar");
    }

    /**
     * Convert the given character to a single glyph index.
     * Note that this function assumes that there is a one-to-one mapping between
     * the given character and a glyph; for complex scripts this might not be the case.
     * @param charCode
     * @return
     */
    public int charToGlyphIndex(int charCode) {
        Integer index = (Integer) scriptObjectMirror.callMember("charToGlyphIndex", StringUtils.fromCharCode(charCode));
        if (index == null) {
            return -1;
        }

        return index;
    }

    /**
     * Convert the given character to a single Glyph object.
     * Note that this function assumes that there is a one-to-one mapping between
     * the given character and a glyph; for complex scripts this might not be the case.
     * @param charCode
     * @return
     */
    public GlyphData charToGlyph(int charCode) {
        int index = charToGlyphIndex(charCode);
        return getGlyphByDefault(index);
    }

    /**
     * Gets the index of a glyph by name.
     * {@link GlyphNames#nameToGlyphIndex(String)}
     * @param name
     * @return
     */
    public int nameToGlyphIndex(String name) {
        return this.glyphNames.nameToGlyphIndex(name);
    }

    /**
     * Convert the given glyph name to a single Glyph object.
     * @param name
     * @return
     */
    public GlyphData nameToGlyph(String name) {
        int index = nameToGlyphIndex(name);
        return getGlyphByDefault(index);
    }

    /**
     * Gets the glyph name by index
     * {@link GlyphNames#glyphIndexToName(int)}
     * @param gid
     * @return
     */
    public String glyphIndexToName(int gid) {
        return this.glyphNames.glyphIndexToName(gid);
    }

    private GlyphData getGlyphByDefault(int index) {
        GlyphData glyphData = glyphs.get(index);
        if (glyphData == null) {
            //.notdef
            return glyphs.get(0);
        }
        return glyphData;
    }

    public GlyphNames getGlyphNames() {
        return glyphNames;
    }

    public void setGlyphNames(GlyphNames glyphNames) {
        this.glyphNames = glyphNames;
    }

    public GlyphDataList getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(GlyphDataList glyphs) {
        this.glyphs = glyphs;
    }

    public int getNumGlyphs() {
        return numGlyphs;
    }

    public void setNumGlyphs(int numGlyphs) {
        this.numGlyphs = numGlyphs;
    }

    public boolean isCIDFont() {
        return isCIDFont;
    }

    public void setCIDFont(boolean CIDFont) {
        isCIDFont = CIDFont;
    }

    public int getDefaultWidthX() {
        return defaultWidthX;
    }

    public void setDefaultWidthX(int defaultWidthX) {
        this.defaultWidthX = defaultWidthX;
    }

    public int getNominalWidthX() {
        return nominalWidthX;
    }

    public void setNominalWidthX(int nominalWidthX) {
        this.nominalWidthX = nominalWidthX;
    }

    public int getUnitsPerEm() {
        return unitsPerEm;
    }

    public void setUnitsPerEm(int unitsPerEm) {
        this.unitsPerEm = unitsPerEm;
    }

    public int getAscender() {
        return ascender;
    }

    public void setAscender(int ascender) {
        this.ascender = ascender;
    }

    public int getDescender() {
        return descender;
    }

    public void setDescender(int descender) {
        this.descender = descender;
    }

    public String getOutlinesFormat() {
        return outlinesFormat;
    }

    public void setOutlinesFormat(String outlinesFormat) {
        this.outlinesFormat = outlinesFormat;
    }

    public Map<String, Object> getTables() {
        return tables;
    }

    public void setTables(Map<String, Object> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return "Font{" +
                "numGlyphs=" + numGlyphs +
                ", isCIDFont=" + isCIDFont +
                ", defaultWidthX=" + defaultWidthX +
                ", nominalWidthX=" + nominalWidthX +
                ", unitsPerEm=" + unitsPerEm +
                ", ascender=" + ascender +
                ", descender=" + descender +
                ", outlinesFormat='" + outlinesFormat + '\'' +
                ", glyphNames=" + glyphNames +
                ", glyphs=" + glyphs +
                ", tables=" + tables +
                '}';
    }
}
