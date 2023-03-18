# How to format mapping and config JSON files
To convert one texture/resource pack format to another you will need 2 mapping files and one config file.

The first mapping is for the input pack and the second for the output pack.

The mapping JSON lets the program know in which directory the files are and what they are named.
The order of the files matters! 

For example, if you want to convert a 1.6.1 Resource Pack into a 1.5.2 Texture Pack, you will have:
    
    mappings/1.6.1.json
    mappings/1.5.2.json
    
    config/1.6.1.json

Make sure that the config file has the same name as your input mapping!

## Example of input Mapping JSON

    {
      "version": "1.6.1",
      "pack_format": 1,
      "desc": "Description goes here",
      "vanilla": {
        "folders": {
          "mob" : "assets/minecraft/textures/entity/"
        },
        "textures": {
          "mob" : [
            ["witch"],
            ["zombie_pigman"],
            ["cat/black"]
          ]
        }
      },
      "btw": {
        "folders": {
          "wood" : "assets/minecraft/textures/blocks/"
        },
        "textures": {
          "wood" : [
            ["oak_planks"]
          ]
        }
      }
    }

## Example of output Mapping JSON

    {
      "version": "1.5.2",
      "pack_format": 0,
      "desc": "Description goes here",
      "vanilla": {
        "folders": {
          "mob" : "mob/"
        },
        "textures": {
          "mob" : [
            ["villager/witch"],
            ["pigzombie"],
            ["cat_black"]
          ]
        }
      },
      "btw": {
        "folders": {
          "wood" : "textures/blocks/"
        },
        "textures": {
          "wood" : [
            ["fcBlockColumnWoodOak_side", ".png", 90]
          ]
        }
      }
    }


`"version"` holds the mc version (used in the console log)

`"pack_format"` holds the pack format and is compared with the format found in pack.mcmeta

`"desc"` holds a small description (used in the console log)

`"vanilla"` corresponds to the String in the config file. You may rename this to whatever you want, but it needs to be the same as in the config files. You may have more than one, but they need to have different names.

`"directory"` holds all the directory names

`"mob"` has the actual directory name (Can be named whatever).

`"files` holds all the file names

`"mob"` has the file names. Needs to match the directory.

`["cat/black",".png", 90]` describes the texture file. First is the texture name (may include a parent directory), second is the file format (can be left out, if the file doesn't need any roation), and third the degree of rotation should be applied (requires the format to be ".png" and will only be read if the mapping is used as the output).

## Example of config JSON

    {   
      "configs": [
        "vanilla", "btw"
      ],
      "vanilla": {
        "mob": true
      },
      "btw:{
        "wood": true
      }
    }

`"configs"` holds all the mapping names (Needs to match with the mapping JSON, see above)

`"vanilla"` is a list of options (Name needs to match with the config above). Add to here if you want to be able to enable or disable the conversion of this directory in the Options window. If it is not present here it is true by default and can't be toggled in the Options Window.