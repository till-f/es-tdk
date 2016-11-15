package fzi.mottem.runtime.rtgraph.XML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import fzi.mottem.runtime.rtgraph.Constants;
import fzi.mottem.runtime.rtgraph.SetupUtilities;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.views.GraphView;

public class ProfileUtils {

	static JAXBContext GVContext;
	static JAXBContext DBoardContext;
	static Marshaller dashboardMarshaller;
	static Unmarshaller dashboardUnMarshaller;
	static Marshaller graphViewMarshaller;
	static Unmarshaller graphViewUnMarshaller;

	static {
		init();
	}

	private ProfileUtils() {

	}

	/**
	 * Init the static members of this class; No operations can take place
	 * before calling this method.
	 */
	public static void init() {

		try {

			GVContext = JAXBContext.newInstance(GraphViewRepresentation.class);
			graphViewMarshaller = GVContext.createMarshaller();
			graphViewMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			graphViewUnMarshaller = GVContext.createUnmarshaller();

			DBoardContext = JAXBContext.newInstance(DashboardRepresentation.class);
			dashboardMarshaller = DBoardContext.createMarshaller();
			dashboardMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			dashboardUnMarshaller = DBoardContext.createUnmarshaller();

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public static void saveDashboardXML(DashboardRepresentation repr, String OSPath) {
		try {
			repr.setPath(OSPath);

			// repr.setName(OSPath.substring(OSPath.lastIndexOf(File.separator)
			// + 1));

			System.out.println("Saving Dashboard xml data for " + repr.getPath());

			if (OSPath.endsWith(Constants.EXTENSION_DASHBOARD)) {
				dashboardMarshaller.marshal(repr, new File(OSPath));
			} else {
				dashboardMarshaller.marshal(repr, new File(OSPath + Constants.EXTENSION_DASHBOARD));
			}

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveDashboardXML(DashboardRepresentation repr, String OSPath, String setName) {
		try {
			repr.setPath(OSPath);

			//repr.setName(setName);

			System.out.println("Saving Dashboard xml data for " + repr.getPath());

			if (OSPath.endsWith(Constants.EXTENSION_DASHBOARD)) {
				dashboardMarshaller.marshal(repr, new File(OSPath));
			} else {
				dashboardMarshaller.marshal(repr, new File(OSPath + Constants.EXTENSION_DASHBOARD));
			}

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveDashboardXML(DashboardRepresentation repr) {
		try {
			System.out.println("Saving Dashboard xml data for " + repr.getPath());
			if (repr.getPath().endsWith(Constants.EXTENSION_DASHBOARD)) {
				dashboardMarshaller.marshal(repr, new File(repr.getPath()));
			} else {
				dashboardMarshaller.marshal(repr, new File(repr.getPath() + Constants.EXTENSION_DASHBOARD));
			}

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean saveGraphViewXML(GraphViewRepresentation repr, String OSPath) {
		boolean saved = true;
		try {
			System.out.println("Saving GraphView xml data for " + OSPath);

			repr.setPath(OSPath);
			if (OSPath.endsWith(Constants.EXTENSION_GRAPHVIEW)) {
				graphViewMarshaller.marshal(repr, new File(OSPath));
				
			} else {
				graphViewMarshaller.marshal(repr, new File(OSPath + Constants.EXTENSION_GRAPHVIEW));
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saved = false;
		}
		return saved;
	}

	public static boolean saveGraphViewXML(GraphViewRepresentation representation, String OSPath, String newName) {
		boolean saved = true;
		try {
			System.out.println("Saving GraphView xml data for " + newName);
			// graphViewMarshaller.marshal(repr, System.out);
			representation.setPath(OSPath);
			//representation.setName(newName);
			if (OSPath.endsWith(Constants.EXTENSION_GRAPHVIEW)) {

				graphViewMarshaller.marshal(representation, new File(OSPath));
			} else {
				graphViewMarshaller.marshal(representation, new File(OSPath + Constants.EXTENSION_GRAPHVIEW));
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saved = false;;
		}
		return saved;
	}

	public static boolean saveGraphViewXML(GraphViewRepresentation repr) {
		boolean saved = true;
		try {
			System.out.println("Saving GraphView xml data for " + repr.getPath());
			// graphViewMarshaller.marshal(repr, System.out);

			if (repr.getPath().endsWith(".graphview")) {
				graphViewMarshaller.marshal(repr, new File(repr.getPath()));
			} else {
				graphViewMarshaller.marshal(repr, new File(repr.getPath() + Constants.EXTENSION_GRAPHVIEW));
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saved = false;
		}
		return saved;
	}

	public static ArrayList<String> gatherProfileNames() {
		ArrayList<String> profiles = new ArrayList<String>();

		File dir = new File(SetupUtilities.workspaceDirectory.getAbsolutePath() + Constants.profiles_path);

		File[] allFiles = dir.listFiles();

		for (File f : allFiles) {
			if (f.getName().endsWith("xml")) {
				profiles.add(f.getName());
			}
		}
		return profiles;
	}

	public static DashboardRepresentation getDashboardModel(String representationOSPath) {
		DashboardRepresentation representation = new DashboardRepresentation();

		try {
			try {
				try {
					representation = (DashboardRepresentation) dashboardUnMarshaller
							.unmarshal(new FileReader(representationOSPath));
				} catch (UnmarshalException e) {
					representation = new DashboardRepresentation();
				}

				representation.setPath(representationOSPath);

			} catch (JAXBException e) {
				System.out.println(representationOSPath + " is empty or does not represent the Dashboard format.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return representation;
	}

	public static GraphViewRepresentation getGraphViewModel(String representationOSPath) {
		GraphViewRepresentation representation = new GraphViewRepresentation();

		try {
			try {

				try {
					representation = (GraphViewRepresentation) graphViewUnMarshaller
							.unmarshal(new FileReader(representationOSPath));
				} catch (UnmarshalException e) {
					representation = new GraphViewRepresentation();
				}
				representation.setPath(representationOSPath);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return representation;
	}

}
