# Help Guide

- [Usage](#usage)
- [Formatting](#formatting)
- [Settings File](#settings-file)

## Usage

1. Download the Latest Release [here](https://github.com/BTW-Community/TexturePackConverter/releases/latest)
2. Unzip the downloaded zip file. The folder should look like this:
```
    ├── TexturePackConverter_1.0.0/
    ┊  ├── TPC_1.0.0.jar
       ├── settings.json
       ├── configs/
       │  ├── 1.5.2.json
       │  ├── 1.6.1-1.8.9.json
       │  └── ...
       └── mappings/
          ├── 1.5.2.json
          ├── 1.6.1-1.8.9.json
          └── ...
```
3. Run `TPC_1.0.0.jar`
4. Under `Import As` select a Texture or Resource Pack you want to convert by pressing the `...` to navigate to your Directory (**Currently selecting the Texture Pack Zip is disabled**). Alternatively you can also just paste in the path of a directory in the designated field. The last 5 selected directories are available in the dropdown.
5. Choose the Mappings for the MC version of your Texture or Resource Pack in the left dropdown menu.
6. Under `Export As` select which Version you want to convert to.
7. Choose the Location and Name of the converted Pack by pasting the path in the designated field or use the `...` button to open a dialog to do this for you. If you just enter a name without a specified location it will be saved to the temp folder.
8. If you want to change any of the Options, click the `Options` button to do so.
9. Finally press `Convert`
10. Once the conversion has finished you can click the `Open` button to open the directory you had chosen to save to.
11. Have fun!

## Formatting
Check out the [Formatting Guide](FORMATTING.md) on how to format config and mapping JSON files.

## Settings File
The Settings JSON file keeps track of various settings that can be changed with TPC.

`currentLanguage`(String): The current selected language. Currently only the following are supported:
- "en_US" (English)
- "de_DE" (German)
- "fr_FR" (French)
- "lu_LU" (Luxembourgish) - coming soon!

`darkmode` (boolean): Enables or disables dark mode.

`startUp_openLog` (boolean): If the Console Log should be opened on start up

`startUp_checkUpdate` (boolean): If you want to check for updates when you start TPC.

`files_input`(array): Keeps track of the last 5 opened Texture/Resource Packs

`files_output`(array): Keeps track of the last 5 save Locations