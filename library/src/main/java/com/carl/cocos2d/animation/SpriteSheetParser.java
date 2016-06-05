package com.carl.cocos2d.animation;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSNumber;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Parser class used to retrieve sprite sheet data from a plist file
 * Created by Carl on 2016/6/2.
 */
public class SpriteSheetParser {
    private final FileOpener mFileOpener;
    private SpriteSheet mResult;

    public SpriteSheetParser(FileOpener fileOpener) {
        this.mFileOpener = fileOpener;
    }

    public SpriteSheet getResult() {
        return mResult;
    }

    public boolean parse(String spriteSheetPath) {
        try {
            doParse(spriteSheetPath);
            return true;
        } catch (IOException | ParserConfigurationException | SAXException | ParseException | PropertyListFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void doParse(String spriteSheetPath) throws IOException, ParserConfigurationException,
            ParseException, SAXException, PropertyListFormatException {
        InputStream spriteSheetInput = mFileOpener.open(spriteSheetPath);
        NSDictionary root = (NSDictionary) PropertyListParser.parse(spriteSheetInput);
        mResult = new SpriteSheet();

        NSDictionary framesDict = (NSDictionary) root.get("frames");
        List<Sprite> frames = new ArrayList<>();
        String[] keys = framesDict.allKeys();
        for (String key : keys) {
            NSDictionary frameDict = (NSDictionary) framesDict.get(key);
            Sprite sprite = new Sprite();
            sprite.setName(key);
            NSObject[] aliasesNsObj = ((NSArray) frameDict.get("aliases")).getArray();
            List<String> aliases = new ArrayList<>();
            for (NSObject alias : aliasesNsObj) {
                String aliasStr = ((NSString) alias).getContent();
                aliases.add(aliasStr);
            }

            String spriteColorRectStr = ((NSString)frameDict.get("spriteColorRect")).getContent();
            Rect spriteColorRect = Rect.fromString(spriteColorRectStr);
            sprite.setSpriteColorRect(spriteColorRect);

            String spriteOffSetStr = ((NSString)frameDict.get("spriteOffset")).getContent();
            Point spriteOffset = Point.fromString(spriteOffSetStr);
            sprite.setSpriteOffset(spriteOffset);

            String spriteSizeStr = ((NSString)frameDict.get("spriteSize")).getContent();
            Size spriteSize = Size.fromString(spriteSizeStr);
            sprite.setSpriteSize(spriteSize);

            String spriteSourceSizeStr = ((NSString)frameDict.get("spriteSourceSize")).getContent();
            Size spriteSourceSize = Size.fromString(spriteSourceSizeStr);
            sprite.setSpriteSourceSize(spriteSourceSize);

            boolean spriteTrimmed = ((NSNumber)frameDict.get("spriteTrimmed")).boolValue();
            sprite.setSpriteTrimmed(spriteTrimmed);

            String textureRectStr = ((NSString)frameDict.get("textureRect")).getContent();
            Rect textureRect = Rect.fromString(textureRectStr);
            sprite.setTextureRect(textureRect);

            boolean textureRotated = ((NSNumber)frameDict.get("textureRotated")).boolValue();
            sprite.setTextureRotated(textureRotated);

            frames.add(sprite);

            for (String alias : aliases) {
                Sprite clone = sprite.makeCopy();
                clone.setName(alias);
                frames.add(clone);
            }
        }

        mResult.setFrames(frames);

        NSDictionary metadataDict = (NSDictionary) root.get("metadata");
        MetaData metaData = new MetaData();
        String version = ((NSString) metadataDict.get("version")).getContent();
        metaData.setVersion(version);

        int format = ((NSNumber) metadataDict.get("format")).intValue();
        metaData.setFormat(format);

        String sizeStr = ((NSString) metadataDict.get("size")).getContent();
        Size size = Size.fromString(sizeStr);
        metaData.setSize(size);

        String name = ((NSString)metadataDict.get("name")).getContent();
        metaData.setName(name);

        boolean premultipliedAlpha = ((NSNumber) metadataDict.get("premultipliedAlpha")).boolValue();
        metaData.setPremultipliedAlpha(premultipliedAlpha);

        NSDictionary targetDict = ((NSDictionary) metadataDict.get("target"));
        Target target = new Target();

        name = ((NSString)targetDict.get("name")).getContent();
        target.setName(name);

        String textureFileName = ((NSString)targetDict.get("textureFileName")).getContent();
        target.setTextureFileName(textureFileName);

        String textureFileExtension = ((NSString)targetDict.get("textureFileExtension")).getContent();
        target.setTextureFileExtension(textureFileExtension);

        String coordinatesFileName = ((NSString)targetDict.get("coordinatesFileName")).getContent();
        target.setCoordinatesFileName(coordinatesFileName);

        String coordinatesFileExtension = ((NSString)targetDict.get("coordinatesFileExtension")).getContent();
        target.setCoordinatesFileExtension(coordinatesFileExtension);

        premultipliedAlpha = ((NSNumber) targetDict.get("premultipliedAlpha")).boolValue();
        target.setPremultipliedAlpha(premultipliedAlpha);

        metaData.setTarget(target);

        mResult.setMetaData(metaData);
    }


}
