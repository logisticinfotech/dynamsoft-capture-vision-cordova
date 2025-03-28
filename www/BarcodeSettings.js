"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.EnumBarcodeFormat_2 = exports.EnumBarcodeFormat = exports.EnumDBRPresetTemplate = void 0;
var EnumDBRPresetTemplate;
(function (EnumDBRPresetTemplate) {
    /**
    * The default setting for processing the video streaming.
    */
    EnumDBRPresetTemplate[EnumDBRPresetTemplate["DEFAULT"] = 0] = "DEFAULT";
    /**
    * Use this template when processing single barcode under the video streaming.
    */
    EnumDBRPresetTemplate[EnumDBRPresetTemplate["VIDEO_SINGLE_BARCODE"] = 1] = "VIDEO_SINGLE_BARCODE";
    /**
    * Use this template for higher speed when processing the video streaming.
    */
    EnumDBRPresetTemplate[EnumDBRPresetTemplate["VIDEO_SPEED_FIRST"] = 2] = "VIDEO_SPEED_FIRST";
    /**
    * Use this template for higher read rate when processing the video streaming.
    */
    EnumDBRPresetTemplate[EnumDBRPresetTemplate["VIDEO_READ_RATE_FIRST"] = 3] = "VIDEO_READ_RATE_FIRST";
    /**
    * Use this template for higher speed when processing a still image.
    */
    EnumDBRPresetTemplate[EnumDBRPresetTemplate["IMAGE_SPEED_FIRST"] = 4] = "IMAGE_SPEED_FIRST";
    /**
    * Use this template for higher read rate when processing a still image.
    */
    EnumDBRPresetTemplate[EnumDBRPresetTemplate["IMAGE_READ_RATE_FIRST"] = 5] = "IMAGE_READ_RATE_FIRST";
    /**
    * The default setting for processing a still image.
    */
    EnumDBRPresetTemplate[EnumDBRPresetTemplate["IMAGE_DEFAULT"] = 6] = "IMAGE_DEFAULT";
})(EnumDBRPresetTemplate = exports.EnumDBRPresetTemplate || (exports.EnumDBRPresetTemplate = {}));
/**
* The first group of barcode formats.
*/
var EnumBarcodeFormat;
(function (EnumBarcodeFormat) {
    EnumBarcodeFormat[EnumBarcodeFormat["BF_ALL"] = -29360129] = "BF_ALL";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_ONED"] = 3147775] = "BF_ONED";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_GS1_DATABAR"] = 260096] = "BF_GS1_DATABAR";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_CODE_39"] = 1] = "BF_CODE_39";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_CODE_128"] = 2] = "BF_CODE_128";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_CODE_93"] = 4] = "BF_CODE_93";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_CODABAR"] = 8] = "BF_CODABAR";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_CODE_11"] = 2097152] = "BF_CODE_11";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_ITF"] = 16] = "BF_ITF";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_EAN_13"] = 32] = "BF_EAN_13";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_EAN_8"] = 64] = "BF_EAN_8";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_UPC_A"] = 128] = "BF_UPC_A";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_UPC_E"] = 256] = "BF_UPC_E";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_INDUSTRIAL_25"] = 512] = "BF_INDUSTRIAL_25";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_CODE_39_EXTENDED"] = 1024] = "BF_CODE_39_EXTENDED";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_GS1_DATABAR_OMNIDIRECTIONAL"] = 2048] = "BF_GS1_DATABAR_OMNIDIRECTIONAL";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_GS1_DATABAR_TRUNCATED"] = 4096] = "BF_GS1_DATABAR_TRUNCATED";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_GS1_DATABAR_STACKED"] = 8192] = "BF_GS1_DATABAR_STACKED";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_GS1_DATABAR_STACKED_OMNIDIRECTIONAL"] = 16384] = "BF_GS1_DATABAR_STACKED_OMNIDIRECTIONAL";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_GS1_DATABAR_EXPANDED"] = 32768] = "BF_GS1_DATABAR_EXPANDED";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_GS1_DATABAR_EXPANDED_STACKED"] = 65536] = "BF_GS1_DATABAR_EXPANDED_STACKED";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_GS1_DATABAR_LIMITED"] = 131072] = "BF_GS1_DATABAR_LIMITED";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_PATCHCODE"] = 262144] = "BF_PATCHCODE";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_PDF417"] = 33554432] = "BF_PDF417";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_QR_CODE"] = 67108864] = "BF_QR_CODE";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_DATAMATRIX"] = 134217728] = "BF_DATAMATRIX";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_AZTEC"] = 268435456] = "BF_AZTEC";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_MAXICODE"] = 536870912] = "BF_MAXICODE";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_MICRO_QR"] = 1073741824] = "BF_MICRO_QR";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_MICRO_PDF417"] = 524288] = "BF_MICRO_PDF417";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_GS1_COMPOSITE"] = -2147483648] = "BF_GS1_COMPOSITE";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_MSI_CODE"] = 1048576] = "BF_MSI_CODE";
    EnumBarcodeFormat[EnumBarcodeFormat["BF_NULL"] = 0] = "BF_NULL";
})(EnumBarcodeFormat = exports.EnumBarcodeFormat || (exports.EnumBarcodeFormat = {}));
/**
* The second group of barcode formats.
*/
var EnumBarcodeFormat_2;
(function (EnumBarcodeFormat_2) {
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_AUSTRALIANPOST"] = 8388608] = "BF2_AUSTRALIANPOST";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_DOTCODE"] = 2] = "BF2_DOTCODE";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_NONSTANDARD_BARCODE"] = 1] = "BF2_NONSTANDARD_BARCODE";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_NULL"] = 0] = "BF2_NULL";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_PHARMACODE"] = 12] = "BF2_PHARMACODE";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_PHARMACODE_ONE_TRACK"] = 4] = "BF2_PHARMACODE_ONE_TRACK";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_PHARMACODE_TWO_TRACK"] = 8] = "BF2_PHARMACODE_TWO_TRACK";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_PLANET"] = 4194304] = "BF2_PLANET";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_POSTALCODE"] = 32505856] = "BF2_POSTALCODE";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_POSTNET"] = 2097152] = "BF2_POSTNET";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_RM4SCC"] = 16777216] = "BF2_RM4SCC";
    EnumBarcodeFormat_2[EnumBarcodeFormat_2["BF2_USPSINTELLIGENTMAIL"] = 1048576] = "BF2_USPSINTELLIGENTMAIL";
})(EnumBarcodeFormat_2 = exports.EnumBarcodeFormat_2 || (exports.EnumBarcodeFormat_2 = {}));
