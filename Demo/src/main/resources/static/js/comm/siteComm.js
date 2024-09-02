/**
 * 일반적인 공통 함수
 */

	$(function() {
		//로딩바 숨김
		$(".spinner-container").hide();
	});
	const G_TOKEN_KEY = 'AccessKeyJwt';
	/**
	 * LocalStorage 관리
	 */
	function fn_SetLocalStorage(key, value){
		localStorage.setItem(key, value);
	}
	
	function fn_GetLocalStorage(key){
		return localStorage.getItem(key);
	}
	
	function fn_DelLocalStorage(key){
		 localStorage.removeItem(key);
	}
	
	/**
	 * SessionStorage 관리
	 */
	function fn_SetSessionStorage(key, value){
		sessionStorage.setItem(key, value);
	}
	
	function fn_GetSessionStorage(key){
		return sessionStorage.getItem(key);
	}
	
	function fn_DelSessionStorage(key){
		 sessionStorage.removeItem(key);
	}

	function fn_getTodayYYYYMMDD() {
		let today = new Date(); 
		let year = today.getFullYear();
		let month = (today.getMonth() + 1).toString().padStart(2, '0');
		let day = today.getDate().toString().padStart(2, '0');
		return `${year}${month}${day}`;
	}

	function fn_getTodayYYYY_MM_DD() {
		let today = new Date(); 
		let year = today.getFullYear();
		let month = (today.getMonth() + 1).toString().padStart(2, '0');
		let day = today.getDate().toString().padStart(2, '0');
		return `${year}-${month}-${day}`;
	}

	const changeXSS =(str) => {
	    return str.replaceAll('&lt;','<').replaceAll('&gt;', '>').replaceAll('&#40;', '(').replaceAll('&#41;', ')').replaceAll('&apos;', '\'').replaceAll('&amp;', '&');
	}


	//nowPage가 시작. 최대 5개까지만 보여주기.
	function fn_makePaging(nowPage,totalCnt,pagePerCnt,divName,fn) {
//			$("#paging").empty();
		$('#'+divName).empty();
		if(totalCnt > 0) {
			
			let showPagingCnt = 5;//페이징은 5개까지만
			let maxPagingCnt = 0;//
			
			let totalPageCnt = Math.floor(totalCnt / pagePerCnt) ;
			let remainder = totalCnt % pagePerCnt;
			if ( remainder != 0) {
				totalPageCnt += 1; 
			}
			if(nowPage > totalPageCnt) {
				nowPage = totalPageCnt;
			}
			
			if( (nowPage + showPagingCnt - 1) < totalPageCnt ) {
				maxPagingCnt = nowPage + showPagingCnt -1;
			} else {
				maxPagingCnt = totalPageCnt;
			}
			
//			console.log(totalPageCnt);
			
			let pagingHtml =  '';
			if( nowPage == 1 ) {
				pagingHtml = pagingHtml + '';
			} else if ( nowPage > 1) {
				pagingHtml = pagingHtml + '<a href="javascript:'+fn+'(1);" ><span > 처음 </span></a>&nbsp;';
				if ( nowPage >= 2) {
					pagingHtml = pagingHtml + '<a href="javascript:'+fn+'('+(nowPage-1)+');" ><span > 이전 </span></a>&nbsp;';
				}
			}
			
			for(var i = nowPage ; i <= maxPagingCnt ; i++) {
				if( i == nowPage) {
					pagingHtml = pagingHtml + '<strong> '+nowPage+' </strong>&nbsp;';
				} else {
					pagingHtml = pagingHtml + '<a href="javascript:'+fn+'('+i+');" > '+i+' </a>&nbsp;';
				}
			}
			if ( nowPage < totalPageCnt) {
				pagingHtml = pagingHtml + '<a href="javascript:'+fn+'('+(nowPage+1)+');" ><span > 다음 </span></a>&nbsp;';
			}
			if ( nowPage < totalPageCnt) {
				pagingHtml = pagingHtml + '<a href="javascript:'+fn+'('+totalPageCnt+');" ><span > 마지막 </span></a>';
			}
			
			$('#'+divName).append(pagingHtml);
		}

	}
