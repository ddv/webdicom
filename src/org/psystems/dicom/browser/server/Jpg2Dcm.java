/*
# jpg2dcm Default Configuration for encapsulating JPEG Baseline streams into
# DICOM Secondary Capture Image objects
# (s. DICOM Part 3, A.32.7 Video Photographic Image IOD)

# Patient Module Attributes
# Patient's Name
00100010:
# Patient ID
00100020:
# Issuer of Patient ID
#00100021:
# Patient's Birth Date
00100030:
# Patient's Sex
00100040:

# General Study Module Attributes
# Study Instance UID
#0020000D:
# Study Date
00080020:
# Study Time
00080030:
# Referring Physician's Name
00080090:
# Study ID
00200010:
# Accession Number
00080050:
# Study Description
#00081030:

# General Series Module Attributes
# Modality
00080060:OT
# Series Instance UID
#0020,000E:
# Series Number
00200011:1

# General Equipment Module Attributes
# Manufacturer
00080070:

# SC Equipment Module Attributes
# Conversion Type
00080064:SI

# General Image Module Attributes
# Instance Number
00200013:1

# Image Pixel Module Attributes (detected from JPEG file, if not specified)
# Samples per Pixel
#00280002:3
# Photometric Interpretation
#00280004:YBR_FULL_422
# Planar Configuration
#00280006:0
# Rows
#00280010:
# Columns
#00280011:
# Bits Allocated
#00280100:8
# Bits Stored
#00280101:8
# High Bit
#00280102:7
# Pixel Representation
#00280103:0

# SOP Common Module Attributes
# SOP Class UID
00080016:1.2.840.10008.5.1.4.1.1.7
# SOP Instance UID
#00080018

 */
package org.psystems.dicom.browser.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.util.UIDUtils;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 12455 $ $Date: 2009-11-26 12:31:31 +0100 (Thu, 26 Nov
 *          2009) $
 * @since Apr 1, 2006
 * 
 */
public class Jpg2Dcm {

	private static final String USAGE = "jpg2dcm [Options] <jpgfile> <dcmfile>";

	private static final String DESCRIPTION = "Encapsulate JPEG Image into DICOM Object.\nOptions:";

	private static final String EXAMPLE = "--\nExample 1: Encapulate JPEG Image verbatim with default values "
			+ "for mandatory DICOM attributes into DICOM Secondary Capture Image:"
			+ "\n$ jpg2dcm image.jpg image.dcm"
			+ "\n--\nExample 2: Encapulate JPEG Image without application segments "
			+ "and additional DICOM attributes to mandatory defaults into DICOM "
			+ "Image Object:"
			+ "\n$ jpg2dcm --no-appn -c patattrs.cfg homer.jpg image.dcm"
			+ "\n--\nExample 3: Encapulate MPEG2 Video with specified DICOM "
			+ "attributes into DICOM Video Object:"
			+ "\n$ jpg2dcm --mpeg -C mpg2dcm.cfg video.mpg video.dcm";

	private static final String LONG_OPT_CHARSET = "charset";

	private static final String OPT_CHARSET_DESC = "Specific Character Set code string, ISO_IR 100 by default";

	private static final String OPT_AUGMENT_CONFIG_DESC = "Specifies DICOM attributes included additional to mandatory defaults";

	private static final String OPT_REPLACE_CONFIG_DESC = "Specifies DICOM attributes included instead of mandatory defaults";

	private static final String LONG_OPT_TRANSFER_SYNTAX = "transfer-syntax";

	private static final String OPT_TRANSFER_SYNTAX_DESC = "Transfer Syntax; 1.2.840.10008.1.2.4.50 (JPEG Baseline) by default.";

	private static final String LONG_OPT_MPEG = "mpeg";

	private static final String OPT_MPEG_DESC = "Same as --transfer-syntax 1.2.840.10008.1.2.4.100 (MPEG2).";

	private static final String LONG_OPT_UID_PREFIX = "uid-prefix";

	private static final String OPT_UID_PREFIX_DESC = "Generate UIDs with given prefix, 1.2.40.0.13.1.<host-ip> by default.";

	private static final String LONG_OPT_NO_APPN = "no-appn";

	private static final String OPT_NO_APPN_DESC = "Exclude application segments APPn from JPEG stream; "
			+ "encapsulate JPEG stream verbatim by default.";

	private static final String OPT_HELP_DESC = "Print this message";

	private static final String OPT_VERSION_DESC = "Print the version information and exit";

	private static int FF = 0xff;

	private static int SOF = 0xc0;

	private static int DHT = 0xc4;

	private static int DAC = 0xcc;

	private static int SOI = 0xd8;

	private static int SOS = 0xda;

	private static int APP = 0xe0;

	private String charset = "ISO_IR 100";

	private String transferSyntax = UID.JPEGBaseline1;

	private byte[] buffer = new byte[8192];

	private int jpgHeaderLen;

	private int jpgLen;

	private boolean noAPPn = false;

	private Properties cfg = new Properties();

	public Jpg2Dcm() {
//		try {
////			cfg.load(Jpg2Dcm.class.getResourceAsStream("jpg2dcm.cfg"));
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
	}

	public final void setCharset(String charset) {
		this.charset = charset;
	}

	private final void setTransferSyntax(String uid) {
		this.transferSyntax = uid;
	}

	private final void setNoAPPn(boolean noAPPn) {
		this.noAPPn = noAPPn;
	}

	
	public void setCfgProperty (String key, String value) {
		cfg.setProperty(key, value);
	}
	
	public void loadConfiguration(File cfgFile, boolean augment)
			throws IOException {
		Properties tmp = augment ? new Properties(cfg) : new Properties();
		InputStream in = new BufferedInputStream(new FileInputStream(cfgFile));
		try {
			tmp.load(in);
		} finally {
			in.close();
		}
		cfg = tmp;
	}

	public void convert(InputStream jpgInputStream,/*File jpgFile,*/ File dcmFile) throws IOException {
		jpgHeaderLen = 0;
		
		
		
		jpgLen = (int) jpgInputStream.available();
		DataInputStream jpgInput = new DataInputStream(jpgInputStream);
//		jpgLen = (int) jpgFile.length();
//		DataInputStream jpgInput = new DataInputStream(new BufferedInputStream(
//				new FileInputStream(jpgFile)));
		
		try {
			
			
			DicomObject attrs = new BasicDicomObject();
			attrs.putString(Tag.SpecificCharacterSet, VR.CS, charset);
			for (Enumeration en = cfg.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				int[] tagPath = Tag.toTagPath(key);
				int last = tagPath.length - 1;
				VR vr = attrs.vrOf(tagPath[last]);
				if (vr == VR.SQ) {
					attrs.putSequence(tagPath);
				} else {
					attrs.putString(tagPath, vr, cfg.getProperty(key));
				}
			}
			//проверка на то что будем писать jpg
			if ( jpgLen>0 && (noAPPn || missingRowsColumnsSamplesPMI(attrs))) {
				readHeader(attrs, jpgInput);
			}
			
			ensureUS(attrs, Tag.BitsAllocated, 8);
			ensureUS(attrs, Tag.BitsStored, attrs.getInt(Tag.BitsAllocated));
			ensureUS(attrs, Tag.HighBit, attrs.getInt(Tag.BitsStored) - 1);
			ensureUS(attrs, Tag.PixelRepresentation, 0);
			ensureUID(attrs, Tag.StudyInstanceUID);
			ensureUID(attrs, Tag.SeriesInstanceUID);
			ensureUID(attrs, Tag.SOPInstanceUID);
			Date now = new Date();
			attrs.putDate(Tag.InstanceCreationDate, VR.DA, now);
			attrs.putDate(Tag.InstanceCreationTime, VR.TM, now);
			attrs.initFileMetaInformation(transferSyntax);
			FileOutputStream fos = new FileOutputStream(dcmFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			DicomOutputStream dos = new DicomOutputStream(bos);
			try {
				dos.writeDicomFile(attrs);
				
				//Запись картинки BEGIN
				if(jpgLen>0) {
				
				dos.writeHeader(Tag.PixelData, VR.OB, -1);
				dos.writeHeader(Tag.Item, null, 0);
				dos.writeHeader(Tag.Item, null, (jpgLen + 1) & ~1);
				dos.write(buffer, 0, jpgHeaderLen);
				int r;
				while ((r = jpgInput.read(buffer)) > 0) {
					dos.write(buffer, 0, r);
				}
				if ((jpgLen & 1) != 0) {
					dos.write(0);
				}
				dos.writeHeader(Tag.SequenceDelimitationItem, null, 0);
				}
				
				//Запись картинки END
				
			} finally {
				dos.close();
			}
		} finally {
			jpgInput.close();
		}
	}

	private boolean missingRowsColumnsSamplesPMI(DicomObject attrs) {
		return !(attrs.containsValue(Tag.Rows)
				&& attrs.containsValue(Tag.Columns)
				&& attrs.containsValue(Tag.SamplesPerPixel) && attrs
				.containsValue(Tag.PhotometricInterpretation));
	}

	private void readHeader(DicomObject attrs, DataInputStream jpgInput)
			throws IOException {
		if (jpgInput.read() != FF || jpgInput.read() != SOI
				|| jpgInput.read() != FF) {
			throw new IOException("JPEG stream does not start with FF D8 FF");
		}
		int marker = jpgInput.read();
		int segmLen;
		boolean seenSOF = false;
		buffer[0] = (byte) FF;
		buffer[1] = (byte) SOI;
		buffer[2] = (byte) FF;
		buffer[3] = (byte) marker;
		jpgHeaderLen = 4;
		while (marker != SOS) {
			segmLen = jpgInput.readUnsignedShort();
			if (buffer.length < jpgHeaderLen + segmLen + 2) {
				growBuffer(jpgHeaderLen + segmLen + 2);
			}
			buffer[jpgHeaderLen++] = (byte) (segmLen >>> 8);
			buffer[jpgHeaderLen++] = (byte) segmLen;
			jpgInput.readFully(buffer, jpgHeaderLen, segmLen - 2);
			if ((marker & 0xf0) == SOF && marker != DHT && marker != DAC) {
				seenSOF = true;
				int p = buffer[jpgHeaderLen] & 0xff;
				int y = ((buffer[jpgHeaderLen + 1] & 0xff) << 8)
						| (buffer[jpgHeaderLen + 2] & 0xff);
				int x = ((buffer[jpgHeaderLen + 3] & 0xff) << 8)
						| (buffer[jpgHeaderLen + 4] & 0xff);
				int nf = buffer[jpgHeaderLen + 5] & 0xff;
				attrs.putInt(Tag.SamplesPerPixel, VR.US, nf);
				if (nf == 3) {
					attrs.putString(Tag.PhotometricInterpretation, VR.CS,
							"YBR_FULL_422");
					attrs.putInt(Tag.PlanarConfiguration, VR.US, 0);
				} else {
					attrs.putString(Tag.PhotometricInterpretation, VR.CS,
							"MONOCHROME2");
				}
				attrs.putInt(Tag.Rows, VR.US, y);
				attrs.putInt(Tag.Columns, VR.US, x);
				attrs.putInt(Tag.BitsAllocated, VR.US, p > 8 ? 16 : 8);
				attrs.putInt(Tag.BitsStored, VR.US, p);
				attrs.putInt(Tag.HighBit, VR.US, p - 1);
				attrs.putInt(Tag.PixelRepresentation, VR.US, 0);
			}
			if (noAPPn & (marker & 0xf0) == APP) {
				jpgLen -= segmLen + 2;
				jpgHeaderLen -= 4;
			} else {
				jpgHeaderLen += segmLen - 2;
			}
			if (jpgInput.read() != FF) {
				throw new IOException("Missing SOS segment in JPEG stream");
			}
			marker = jpgInput.read();
			buffer[jpgHeaderLen++] = (byte) FF;
			buffer[jpgHeaderLen++] = (byte) marker;
		}
		if (!seenSOF) {
			throw new IOException("Missing SOF segment in JPEG stream");
		}
	}

	private void growBuffer(int minSize) {
		int newSize = buffer.length << 1;
		while (newSize < minSize) {
			newSize <<= 1;
		}
		byte[] tmp = new byte[newSize];
		System.arraycopy(buffer, 0, tmp, 0, jpgHeaderLen);
		buffer = tmp;
	}

	private void ensureUID(DicomObject attrs, int tag) {
		if (!attrs.containsValue(tag)) {
			attrs.putString(tag, VR.UI, UIDUtils.createUID());
		}
	}

	private void ensureUS(DicomObject attrs, int tag, int val) {
		if (!attrs.containsValue(tag)) {
			attrs.putInt(tag, VR.US, val);
		}
	}

	

	// private static CommandLine parse(String[] args) {
	// Options opts = new Options();
	//        
	// OptionBuilder.withArgName("code");
	// OptionBuilder.hasArg();
	// OptionBuilder.withDescription(OPT_CHARSET_DESC);
	// OptionBuilder.withLongOpt(LONG_OPT_CHARSET);
	// opts.addOption(OptionBuilder.create());
	//
	// OptionBuilder.withArgName("file");
	// OptionBuilder.hasArg();
	// OptionBuilder.withDescription(OPT_AUGMENT_CONFIG_DESC);
	// opts.addOption(OptionBuilder.create("c"));
	//        
	// OptionBuilder.withArgName("file");
	// OptionBuilder.hasArg();
	// OptionBuilder.withDescription(OPT_REPLACE_CONFIG_DESC);
	// opts.addOption(OptionBuilder.create("C"));
	//                
	// OptionBuilder.withArgName("prefix");
	// OptionBuilder.hasArg();
	// OptionBuilder.withDescription(OPT_UID_PREFIX_DESC);
	// OptionBuilder.withLongOpt(LONG_OPT_UID_PREFIX);
	// opts.addOption(OptionBuilder.create());
	//        
	// OptionBuilder.withArgName("uid");
	// OptionBuilder.hasArg();
	// OptionBuilder.withDescription(OPT_TRANSFER_SYNTAX_DESC);
	// OptionBuilder.withLongOpt(LONG_OPT_TRANSFER_SYNTAX);
	// opts.addOption(OptionBuilder.create());
	//        
	// opts.addOption(null, LONG_OPT_MPEG, false, OPT_MPEG_DESC);
	//
	// opts.addOption(null, LONG_OPT_NO_APPN, false, OPT_NO_APPN_DESC);
	//        
	// opts.addOption("h", "help", false, OPT_HELP_DESC);
	// opts.addOption("V", "version", false, OPT_VERSION_DESC);
	//        
	// CommandLine cl = null;
	// try {
	// cl = new PosixParser().parse(opts, args);
	// } catch (ParseException e) {
	// exit("jpg2dcm: " + e.getMessage());
	// throw new RuntimeException("unreachable");
	// }
	// if (cl.hasOption('V')) {
	// Package p = Jpg2Dcm.class.getPackage();
	// System.out.println("jpg2dcm v" + p.getImplementationVersion());
	// System.exit(0);
	// }
	// if (cl.hasOption('h') || cl.getArgList().size() != 2) {
	// HelpFormatter formatter = new HelpFormatter();
	// formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);
	// System.exit(0);
	// }
	//
	// return cl;
	// }

	private static void exit(String msg) {
		System.err.println(msg);
		System.err.println("Try 'jpg2dcm -h' for more information.");
		System.exit(1);
	}

}
