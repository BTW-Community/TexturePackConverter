# How to format mapping and config JSON files
To convert one texture/resource pack format to another you will need 2 mapping files and one config file.

The first mapping is for the input pack and the second for the output pack.

The mapping JSON lets the program know in which directory the files are and what they are named.
The order of the files matters! 

For example, if you want to convert a 1.5.2 Texture Pack into a 1.6.1-1.8.9 Resource Pack you will have:
    
    ../mappings/1.6.1-1.8.9.json
    ../mappings/1.5.2.json
    
    ../config/1.6.1.json

Make sure that the config file has the same name as your input mapping!

## Example of input Mapping JSON

    {
      "mapping_version": "1.0.0",
      "mapping_desc": "Mappings for 1.6.1 - 1.8.9 (Resource Pack Format 1)",
      "pack_format": 1,
      "vanilla": {
        "directory": {
          "pack": "/"
          "mob": "/assets/minecraft/textures/entity/"
        },
        "files": {
          "pack" : [
            ["pack"],
            ["pack", "format=txt"]
          ],
          "mob" : [
            ["witch"],
            ["zombie_pigman"],
            ["cat/black"]
          ]
        }
      },
      "btw": {
        "directory": {
          "blocks" : "/assets/minecraft/textures/blocks/",
          "items" : "/assets/minecraft/textures/items/"
        },
        "files": {
          "blocks" : [
            ["oak_column", "oak_planks", "rotate=90"],
            ["blood_wood_planks","texture=oak_planks", "tint=aa1e00"]
            ["axle_powered", "format=png.mcmeta"]
          ],
          "items" : [
            ["diamond_ingot", "texture=iron_ingot", "tint=light_blue"],
            ["knitting_needles", "texture=stick", "mirror=true", "overlay=stick"]
          ]
        }
      }
    }

## Example of output Mapping JSON

    {
      "mapping_version": "1.0.0",
      "mapping_desc": "Mappings for 1.5.2"
      "pack_format": 0,
      "vanilla": {
        "folders": {
          "pack" : "/"
          "mob" : "/mob/"
        },
        "files": {
          "pack" : [
            ["pack"],
            ["pack", "format=txt"]
          ],
          "mob" : [
            ["villager/witch"],
            ["pigzombie"],
            ["cat_black"]
          ]
        }
      },
      "btw": {
        "folders": {
          "blocks" : "textures/blocks/"
          "items" : "textures/blocks/"
        },
        "files": {
          "blocks" : [
            ["fcBlockColumnWoodOak_side"],
            ["fcBlockPlanks_blood"],
            ["fcBlockAxle_side_on", "format=txt"]
          ],
          "items" : [
            ["fcItemIngotDiamond"],
            ["fcItemKnittingNeedles"]
          ]
        }
      }
    }


`"mapping_version"` holds the mapping version (used in the console log)

`"mapping_desc"` holds a small description of the mapping (used in the console log)

`"pack_format"` holds the pack format and is compared with the format found in pack.mcmeta

`"vanilla"` corresponds to the String in the config file. You may rename this to whatever you want, but it needs to be the same as in the _**config**_ files. You may have more than one, but they need to have different names.

`"directory"` holds all the directory names

`"mob"` has the actual directory name (Can be named whatever).

`"files` holds all the file names

`"mob"` has the file names. Needs to match the **_directory_** name.

`["textureName", "texture=altTexture", "overlay=overlayTexture", "tint=d76635", "rotate=0", "mirror=false", "format=png"]` describes the texture file. 
* `textureName`: has to the texture name (may include a parent directory seperated by a `/`)
* `texture`:  alternative texture to use if textureName cannot be found.
* `tint`: adds a color multiplier in hex format. Alternatively the default 16 minecraft colors can be accessed via writing the color as a string like: `lime` or `CYAN`.
* `rotate`: rotates the image in deg.
* `mirror`: mirrors the image.
* `overlay`: overlays a texture ontop the textureName or altTexture image.
* `format`: if not specified the default format is png, any format is allowed. Txt and png.mcmeta can be converted into the other.

The modifiers `tint`,`rotate`,`mirror`,`overlay` are executed in that order, tough the order doesn't matter in the mappings.

## Example of config JSON

    {   
      "configs": [
        "vanilla", "btw"
      ],
      "vanilla": {
        "pack": true,
        "mob": true
      },
      "btw:{
        "blocks": true,
        "items": true
      }
    }

`"configs"` holds all the mapping names (Needs to match with the mapping JSON, see above)

`"vanilla"` is a list of options (Name needs to match with the config above). Add to here if you want to be able to enable or disable the conversion of this directory in the Options window. If it is not present here it is true by default and can't be toggled in the Options Window.