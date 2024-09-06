/**
 * 일반적인 공통 함수
 */

	$(function() {
		//로딩바 숨김
		$(".spinner-container").hide();
		g_formSubmitState = false;//페이지 뒤로가기 하면 값 초기화 안되는 문제 해결
	});
	//JWT 토큰 사용시 키값
	const G_TOKEN_KEY = 'AccessKeyJwt';
	
	let g_formSubmitState = false;
	function gfn_submit(formId) {
		let submitForm = $('#'+formId);
		if(!g_formSubmitState) {
			g_formSubmitState = true;
			setTimeout(function() {
				submitForm.attr("onsubmit", "return true");
				submitForm.submit();
				submitForm.attr("onsubmit", "return false");
			},1000);
		} else {
			alert('이미 submit 되었습니다.');
		}
	}

	
	/**
	 * LocalStorage 관리
	 */
	function gfn_SetLocalStorage(key, value){
		localStorage.setItem(key, value);
	}
	
	function gfn_GetLocalStorage(key){
		return localStorage.getItem(key);
	}
	
	function gfn_DelLocalStorage(key){
		 localStorage.removeItem(key);
	}
	
	/**
	 * SessionStorage 관리
	 */
	function gfn_SetSessionStorage(key, value){
		sessionStorage.setItem(key, value);
	}
	
	function gfn_GetSessionStorage(key){
		return sessionStorage.getItem(key);
	}
	
	function gfn_DelSessionStorage(key){
		 sessionStorage.removeItem(key);
	}

	function gfn_getTodayYYYYMMDD() {
		let today = new Date(); 
		let year = today.getFullYear();
		let month = (today.getMonth() + 1).toString().padStart(2, '0');
		let day = today.getDate().toString().padStart(2, '0');
		return `${year}${month}${day}`;
	}

	function gfn_getTodayYYYY_MM_DD() {
		let today = new Date(); 
		let year = today.getFullYear();
		let month = (today.getMonth() + 1).toString().padStart(2, '0');
		let day = today.getDate().toString().padStart(2, '0');
		return `${year}-${month}-${day}`;
	}

	/**
	 * XSS 관련 스크립트 원복 함수
	 * @param str : 원복할 String
	 */
	const gfn_changeXSS =(str) => {
	    return str.replaceAll('&lt;','<').replaceAll('&gt;', '>').replaceAll('&#40;', '(').replaceAll('&#41;', ')').replaceAll('&apos;', '\'').replaceAll('&amp;', '&');
	}


	//nowPage가 시작. 최대 5개까지만 보여주기.
	function gfn_makePaging(nowPage,totalCnt,pagePerCnt,divName,fn) {
		$('#'+divName).empty();
		let numNowPage = Number(nowPage);
		let numTotalCnt = Number(totalCnt);
		let numPagePerCnt = Number(pagePerCnt);
		
		if(numTotalCnt > 0) {
			
			let showPagingCnt = 5;//페이징은 5개까지만
			let maxPagingCnt = 0;//
			
			let totalPageCnt = Math.floor(numTotalCnt / numPagePerCnt) ;
			let remainder = numTotalCnt % numPagePerCnt;
			if ( remainder != 0) {
				totalPageCnt += 1; 
			}
			if(numNowPage > totalPageCnt) {
				numNowPage = totalPageCnt;
			}
			
			if( (numNowPage + showPagingCnt - 1) < totalPageCnt ) {
				maxPagingCnt = numNowPage + showPagingCnt -1;
			} else {
				maxPagingCnt = totalPageCnt;
			}
			
//			console.log(totalPageCnt);
			
			let pagingHtml =  '';
			if( numNowPage == 1 ) {
				pagingHtml = pagingHtml + '';
			} else if ( numNowPage > 1) {
				pagingHtml = pagingHtml + '<a href="javascript:'+fn+'(1);" ><span > 처음 </span></a>&nbsp;';
				if ( numNowPage >= 2) {
					pagingHtml = pagingHtml + '<a href="javascript:'+fn+'('+(numNowPage-1)+');" ><span > 이전 </span></a>&nbsp;';
				}
			}
			
			for(var i = numNowPage ; i <= maxPagingCnt ; i++) {
				if( i == numNowPage) {
					pagingHtml = pagingHtml + '<strong> '+numNowPage+' </strong>&nbsp;';
				} else {
					pagingHtml = pagingHtml + '<a href="javascript:'+fn+'('+i+');" > '+i+' </a>&nbsp;';
				}
			}
			if ( numNowPage < totalPageCnt) {
				pagingHtml = pagingHtml + '<a href="javascript:'+fn+'('+(numNowPage+1)+');" ><span > 다음 </span></a>&nbsp;';
			}
			if ( numNowPage < totalPageCnt) {
				pagingHtml = pagingHtml + '<a href="javascript:'+fn+'('+totalPageCnt+');" ><span > 마지막 </span></a>';
			}
			
			$('#'+divName).append(pagingHtml);
		}

	}
