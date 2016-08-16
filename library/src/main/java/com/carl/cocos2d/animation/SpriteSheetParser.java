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
            List<String> aliases = new ArrayList<>();
            NSArray aliasNsArray = (NSArray) frameDict.get("aliases");
            if (aliasNsArray != null) {
                NSObject[] aliasesNsObj = aliasNsArray.getArray();
                for (NSObject alias : aliasesNsObj) {
                    String aliasStr = ((NSString) alias).getContent();
                    aliases.add(aliasStr);
                }
            }

            NSString spriteColorRectNSString = (NSString) frameDict.get("spriteColorRect");
            if (spriteColorRectNSString != null) {
                String spriteColorRectStr = spriteColorRectNSString.getContent();
                Rect spriteColorRect = Rect.fromString(spriteColorRectStr);
                sprite.setSpriteColorRect(spriteColorRect);
            }
            NSString spriteOffsetNSString = (NSString) frameDict.get("spriteOffset");
            if (spriteOffsetNSString != null) {
                String spriteOffSetStr = spriteOffsetNSString.getContent();
                Point spriteOffset = Point.fromString(spriteOffSetStr);
                sprite.setSpriteOffset(spriteOffset);
            }

            NSString spriteSizeNSString = (NSString) frameDict.get("spriteSize");
            if (spriteSizeNSString != null) {
                String spriteSizeStr = spriteSizeNSString.getContent();
                Size spriteSize = Size.fromString(spriteSizeStr);
                sprite.setSpriteSize(spriteSize);
            }

            NSString spriteSourceSizeNSString = (NSString) frameDict.get("spriteSourceSize");
            if (spriteSourceSizeNSString != null) {
                String spriteSourceSizeStr = spriteSourceSizeNSString.getContent();
                Size spriteSourceSize = Size.fromString(spriteSourceSizeStr);
                sprite.setSpriteSourceSize(spriteSourceSize);
            }

            NSNumber spriteTrimmedNSNumber = (NSNumber) frameDict.get("spriteTrimmed");
            if (spriteTrimmedNSNumber != null) {
                boolean spriteTrimmed = spriteTrimmedNSNumber.boolValue();
                sprite.setSpriteTrimmed(spriteTrimmed);
            }

            NSString textureRectNSString = (NSString) frameDict.get("textureRect");
            if (textureRectNSString != null) {
                String textureRectStr = textureRectNSString.getContent();
                Rect textureRect = Rect.fromString(textureRectStr);
                sprite.setTextureRect(textureRect);
            }

            NSNumber textureRotatedNSNumber = (NSNumber) frameDict.get("textureRotated");
            if (textureRotatedNSNumber != null) {
                boolean textureRotated = textureRotatedNSNumber.boolValue();
                sprite.setTextureRotated(textureRotated);
            }
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
        if (metadataDict != null) {
            NSString versionNSString = (NSString) metadataDict.get("version");
            if (versionNSString != null) {
                String version = versionNSString.getContent();
                metaData.setVersion(version);
            }

            NSNumber formatNSNumber = (NSNumber) metadataDict.get("format");
            if (formatNSNumber != null) {
                int format = formatNSNumber.intValue();
                metaData.setFormat(format);
            }

            NSString sizeNSString = (NSString) metadataDict.get("size");
            if (sizeNSString != null) {
                String sizeStr = sizeNSString.getContent();
                Size size = Size.fromString(sizeStr);
                metaData.setSize(size);
            }

            NSString nameNSString = (NSString) metadataDict.get("name");
            if (nameNSString != null) {
                String name = nameNSString.getContent();
                metaData.setName(name);
            }

            NSNumber premultipliedAlphaNSNumber = (NSNumber) metadataDict.get("premultipliedAlpha");
            if (premultipliedAlphaNSNumber != null) {
                boolean premultipliedAlpha = premultipliedAlphaNSNumber.boolValue();
                metaData.setPremultipliedAlpha(premultipliedAlpha);
            }

            NSDictionary targetDict = ((NSDictionary) metadataDict.get("target"));
            Target target = new Target();

            if (targetDict != null) {
                nameNSString = (NSString) targetDict.get("name");
                String name = nameNSString.getContent();
                target.setName(name);

                NSString textureFileNameNSString = (NSString) targetDict.get("textureFileName");
                if (textureFileNameNSString != null) {
                    String textureFileName = textureFileNameNSString.getContent();
                    target.setTextureFileName(textureFileName);
                }

                NSString textureFileExtensionNSString = (NSString) targetDict.get("textureFileExtension");
                if (textureFileExtensionNSString != null) {
                    String textureFileExtension = textureFileExtensionNSString.getContent();
                    target.setTextureFileExtension(textureFileExtension);
                }

                NSString coordinatesFileNameNSString = (NSString) targetDict.get("coordinatesFileName");
                if (coordinatesFileNameNSString != null) {
                    String coordinatesFileName = coordinatesFileNameNSString.getContent();
                    target.setCoordinatesFileName(coordinatesFileName);
                }

                NSString coordinatesFileExtensionNSString = (NSString) targetDict.get("coordinatesFileExtension");
                if (coordinatesFileExtensionNSString != null) {
                    String coordinatesFileExtension = coordinatesFileExtensionNSString.getContent();
                    target.setCoordinatesFileExtension(coordinatesFileExtension);
                }

                premultipliedAlphaNSNumber = (NSNumber) targetDict.get("premultipliedAlpha");
                if (premultipliedAlphaNSNumber != null) {
                    boolean premultipliedAlpha = premultipliedAlphaNSNumber.boolValue();
                    target.setPremultipliedAlpha(premultipliedAlpha);
                }

                metaData.setTarget(target);
            }

            mResult.setMetaData(metaData);
        }
    }


}
