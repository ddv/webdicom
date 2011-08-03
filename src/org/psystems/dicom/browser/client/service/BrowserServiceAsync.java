/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */
package org.psystems.dicom.browser.client.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.psystems.dicom.browser.client.proxy.DcmTagProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCRequest;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCResponse;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.OOTemplateProxy;
import org.psystems.dicom.browser.client.proxy.PatientsRPCRequest;
import org.psystems.dicom.browser.client.proxy.PatientsRPCResponse;
import org.psystems.dicom.browser.client.proxy.QueryDirectionProxy;
import org.psystems.dicom.browser.client.proxy.RPCDcmProxyEvent;
import org.psystems.dicom.browser.client.proxy.Session;
import org.psystems.dicom.browser.client.proxy.StudyProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BrowserServiceAsync {

    void findStudy(long transactionId, String version, String queryStr, HashMap<String, String> attrs,
	    AsyncCallback<RPCDcmProxyEvent> callback);

    void getDcmTagsFromFile(long transactionId, String version, long idDcmFile,
	    AsyncCallback<ArrayList<DcmTagProxy>> callback);

    void getPatients(PatientsRPCRequest req, AsyncCallback<PatientsRPCResponse> callback);

    void getDcmTags(DcmTagsRPCRequest req, AsyncCallback<DcmTagsRPCResponse> callback);

    void getStudyByID(long transactionId, String version, Long id, AsyncCallback<StudyProxy> callback);

    void getSessionObject(AsyncCallback<Session> callback);

    void getOOTemplates(String modality, AsyncCallback<ArrayList<OOTemplateProxy>> callback);

    void getDirections(QueryDirectionProxy query, AsyncCallback<ArrayList<DirectionProxy>> callback);

    void saveDirection(DirectionProxy drn, AsyncCallback<Void> callback);

    void getStudiesByDirectionID(Long id, AsyncCallback<StudyProxy[]> callback);

}
