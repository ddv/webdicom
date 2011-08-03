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

import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("browser")
public interface BrowserService extends RemoteService {

    /**
     * Поиск исследований
     * 
     * @param transactionId
     * @param version
     * @param queryStr
     * @param attrs
     * @return
     * @throws DefaultGWTRPCException
     */
    RPCDcmProxyEvent findStudy(long transactionId, String version, String queryStr, HashMap<String, String> attrs)
	    throws DefaultGWTRPCException;

    PatientsRPCResponse getPatients(PatientsRPCRequest req) throws DefaultGWTRPCException;

    DcmTagsRPCResponse getDcmTags(DcmTagsRPCRequest req) throws DefaultGWTRPCException;

    ArrayList<DcmTagProxy> getDcmTagsFromFile(long transactionId, String version, long idDcmFile)
	    throws DefaultGWTRPCException;

    StudyProxy getStudyByID(long transactionId, String version, Long id) throws DefaultGWTRPCException;

    /**
     * Получение списка исследований по внутреннему ID-направления
     * 
     * @param id
     * @return
     * @throws DefaultGWTRPCException
     */
    StudyProxy[] getStudiesByDirectionID(Long id) throws DefaultGWTRPCException;

    Session getSessionObject() throws DefaultGWTRPCException;

    //FIXME переименовать в PDF....
    ArrayList<OOTemplateProxy> getOOTemplates(String modality) throws DefaultGWTRPCException;

    ArrayList<DirectionProxy> getDirections(QueryDirectionProxy query) throws DefaultGWTRPCException;

    /**
     * Сохранение направления
     * 
     * @param drn
     * @throws DefaultGWTRPCException
     */
    void saveDirection(DirectionProxy drn) throws DefaultGWTRPCException;
}
