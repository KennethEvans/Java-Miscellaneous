package misc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * TestEclipseLauncher prints out selected plug-ins from a line in an Eclipse
 * launcher. (The launcher files are in the org.eclipse.debug.core directory.)
 * 
 * @author Kenneth Evans, Jr.
 */
public class TestEclipseLauncher
{
    private static String value1 = "org.eclipse.jface@default:default,org.eclipse.persistence.jpa.equinox.weaving@default:false,org.eclipse.jface.databinding@default:default,org.eclipse.core.jobs@default:default,javax.servlet@default:default,org.eclipse.osgi,org.eclipse.swt.win32.win32.x86@default:false,org.eclipse.core.runtime@default:true,org.eclipse.equinox.preferences@default:default,org.eclipse.core.runtime.compatibility.auth@default:default,org.eclipse.core.commands@default:default,org.eclipse.equinox.app@default:default,org.eclipse.equinox.registry@default:default,org.eclipse.core.contenttype@default:default,org.eclipse.ui.workbench@default:default,org.eclipse.core.databinding.property@default:default,com.ibm.icu@default:default,org.eclipse.equinox.common@default:default,org.eclipse.core.expressions@default:default,org.eclipse.osgi.services@default:default,javax.transaction@default:false,org.eclipse.core.databinding.observable@default:default,org.eclipse.core.runtime.compatibility.registry@default:false,org.eclipse.core.databinding@default:default,org.eclipse.ui@default:default,org.eclipse.swt@default:default,org.eclipse.help@default:default";
    private static String value2 = "org.eclipse.equinox.p2.ui@default:default,org.eclipse.jface@default:default,org.eclipse.equinox.simpleconfigurator.manipulator@default:default,org.eclipse.ecf.provider.filetransfer@default:default,org.eclipse.equinox.security.ui@default:default,org.eclipse.equinox.p2.garbagecollector@default:default,org.eclipse.jface.databinding@default:default,org.eclipse.equinox.frameworkadmin@default:default,org.eclipse.equinox.p2.repository.tools@default:default,org.eclipse.equinox.p2.directorywatcher@default:default,org.eclipse.equinox.p2.extensionlocation@default:default,org.apache.commons.logging@default:default,org.eclipse.equinox.p2.touchpoint.eclipse@default:default,org.eclipse.core.jobs@default:default,org.eclipse.osgi@-1:true,org.eclipse.equinox.p2.updatechecker@default:default,org.eclipse.ecf.provider.filetransfer.httpclient.ssl@default:false,org.eclipse.swt.win32.win32.x86@default:false,org.eclipse.equinox.p2.metadata.repository@default:default,org.eclipse.equinox.launcher.win32.win32.x86@default:false,org.eclipse.core.runtime@default:true,org.eclipse.ecf.filetransfer@default:default,org.eclipse.equinox.simpleconfigurator@1:true,org.eclipse.update.configurator@default:true,org.eclipse.equinox.p2.exemplarysetup@default:default,org.sat4j.core@default:default,org.eclipse.equinox.p2.ui.sdk.scheduler@default:default,org.eclipse.equinox.preferences@default:default,org.eclipse.core.runtime.compatibility.auth@default:default,org.sat4j.pb@default:default,org.eclipse.equinox.ds@1:true,org.eclipse.core.commands@default:default,org.eclipse.equinox.app@default:default,org.eclipse.core.databinding.beans@default:default,org.eclipse.ecf.ssl@default:false,org.eclipse.equinox.p2.director.app@default:default,org.eclipse.equinox.registry@default:default,org.eclipse.equinox.util@default:default,org.eclipse.core.contenttype@default:default,org.eclipse.rcp@default:default,org.eclipse.ui.workbench@default:default,org.eclipse.ecf.provider.filetransfer.httpclient@default:default,org.eclipse.equinox.p2.updatesite@default:default,org.eclipse.equinox.p2.core@default:default,org.eclipse.core.databinding.property@default:default,org.apache.commons.httpclient@default:default,org.apache.commons.codec@default:default,org.eclipse.ecf.identity@default:default,org.eclipse.ecf.provider.filetransfer.ssl@default:false,org.eclipse.equinox.frameworkadmin.equinox@default:default,org.eclipse.equinox.p2.metadata.generator@default:default,com.ibm.icu@default:default,org.eclipse.ecf@default:default,org.eclipse.equinox.launcher@default:default,org.eclipse.equinox.p2.reconciler.dropins@default:true,org.eclipse.equinox.common@2:true,org.eclipse.core.expressions@default:default,org.eclipse.osgi.services@default:default,org.eclipse.equinox.security@default:default,org.eclipse.equinox.p2.publisher@default:default,org.eclipse.equinox.p2.ui.sdk@default:default,org.eclipse.equinox.p2.director@default:default,org.eclipse.equinox.p2.repository@default:default,org.eclipse.core.databinding.observable@default:default,org.eclipse.equinox.p2.artifact.repository@default:default,org.eclipse.equinox.p2.touchpoint.natives@default:default,org.eclipse.equinox.p2.metadata@default:default,org.eclipse.equinox.p2.engine@default:default,org.eclipse.core.runtime.compatibility.registry@default:false,org.eclipse.equinox.p2.jarprocessor@default:default,org.eclipse.equinox.p2.console@default:default,org.eclipse.core.databinding@default:default,org.eclipse.ui@default:default,org.eclipse.swt@default:default,org.eclipse.help@default:default";
    private static String value3 = "org.eclipse.equinox.p2.ui@default:default,org.eclipse.jface@default:default,org.eclipse.equinox.simpleconfigurator.manipulator@default:default,org.eclipse.ecf.provider.filetransfer@default:default,org.eclipse.persistence.jpa.equinox.weaving@default:false,org.eclipse.equinox.security.ui@default:default,org.eclipse.equinox.p2.garbagecollector@default:default,org.eclipse.jface.databinding@default:default,org.eclipse.equinox.frameworkadmin@default:default,org.eclipse.core.net@default:default,org.eclipse.core.jobs@default:default,javax.servlet@default:default,org.eclipse.osgi,org.eclipse.swt.win32.win32.x86@default:false,org.eclipse.equinox.p2.metadata.repository@default:default,org.eclipse.core.runtime@default:true,org.eclipse.ecf.filetransfer@default:default,org.eclipse.equinox.simpleconfigurator@default:default,org.eclipse.equinox.p2.exemplarysetup@default:default,org.sat4j.core@default:default,org.eclipse.equinox.preferences@default:default,org.eclipse.core.runtime.compatibility.auth@default:default,org.eclipse.core.net.win32.x86@default:false,org.sat4j.pb@default:default,org.eclipse.core.commands@default:default,org.eclipse.equinox.app@default:default,org.eclipse.equinox.concurrent@default:default,org.eclipse.ecf.ssl@default:false,org.eclipse.equinox.registry@default:default,org.eclipse.core.contenttype@default:default,org.eclipse.ui.workbench@default:default,org.eclipse.equinox.p2.core@default:default,org.eclipse.core.databinding.property@default:default,org.eclipse.ecf.identity@default:default,org.eclipse.ecf.provider.filetransfer.ssl@default:false,org.eclipse.equinox.frameworkadmin.equinox@default:default,com.ibm.icu@default:default,org.eclipse.ecf@default:default,org.eclipse.equinox.common@default:default,org.eclipse.core.expressions@default:default,org.eclipse.osgi.services@default:default,org.eclipse.equinox.security@default:default,org.eclipse.equinox.p2.director@default:default,org.eclipse.equinox.p2.repository@default:default,javax.transaction@default:false,org.eclipse.core.databinding.observable@default:default,org.eclipse.equinox.p2.artifact.repository@default:default,org.eclipse.equinox.security.win32.x86@default:false,org.eclipse.equinox.p2.metadata@default:default,org.eclipse.equinox.p2.engine@default:default,org.eclipse.core.runtime.compatibility.registry@default:false,org.eclipse.equinox.p2.jarprocessor@default:default,org.eclipse.core.databinding@default:default,org.eclipse.ui@default:default,org.eclipse.swt@default:default,org.eclipse.help@default:default";
    private static String values[] = {value1, value3, value2};

    /**
     * @param args
     */
    public static void main(String[] args) {
        String[] split = null;
        List<String> list = null;
        ListIterator<String> iter;

        int i = 0;
        for(String value : values) {
            System.out.println("Set " + (1 + i++));
            split = value.split(",");
            // Convert it to a list so we can sort it
            list = Arrays.asList(split);
            // Sort it
            Collections.sort(list);
            iter = list.listIterator();
            while(iter.hasNext()) {
                String str = iter.next();
                System.out.println(str);
            }
            System.out.println();
        }
        System.out.println("All done");
    }

}
