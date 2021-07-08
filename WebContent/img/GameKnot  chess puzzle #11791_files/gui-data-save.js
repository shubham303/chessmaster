function create_data_save( _div_name, _el_type, _data_url, _save_params, _load_params )
//----------------------------------------
// type:
//	1 = check-box
//----------------------------------------
{
    this.div_name = _div_name;	// container
    this.el_type = _el_type;
    this.data_url = _data_url;
    this.save_params = !_save_params ? [] : _save_params.slice();	// array expected, copy the array
    this.load_params = !_load_params ? [] : _load_params.slice();	// array expected, copy the array
    this.b_modal_msg = 0;
    this.b_loaded = 0;

    this.get_el = function()
    {
	var o = gk_get_el(this.div_name);	// container
	var tag_name;
	var tag_type;
	if (this.el_type==1)	// checkbox
	{
	    tag_name = 'input';
	    tag_type = 'checkbox';
	}
	else
	    return;

	var all = o.getElementsByTagName(tag_name);
	if (!all) return;
	for (var i = 0; i < all.length; i++)
	{
	    var t = String(all[i].type).toLowerCase();
	    if (t!=tag_type) continue;
	    return all[i];	// found it
	}
    };
    
    this.notify_click = function(el)
    {
	if (this.el_type==1)	// checkbox
	{
	    var new_state = el && el.checked ? 1 : 0;
	    this.save(new_state);
	}
    };

    this.save = function(data)
    {
	this.update(1);

	var me = this;
	setTimeout( function() { me.do_save(data); }, 100 );
    }

    this.do_save = function(data)
    {
	var params = this.save_params.slice();	// copy array
	params.push( 'data='+data );
    
	var res = this.req_server( params );
	var b_success = res[0];
	var msg = res[1];
    
	if (msg) 
	{
	    this.b_modal_msg = 0;
	    var ico = b_success ? 'tick-circle.png' : 'minus-circle.png';
	    gui_pop_message( '<img class=img-i src="/img/i/'+ico+'"> '+msg, this.get_el() || document.body, 
			    b_success ? 2000 : 3000, b_success ? 1000 : 2000, 1 );
	}
	if (!b_success)
	{
	    // revert back?
	}
    
	var me = this;
	setTimeout( function() { me.update(); }, 500 );		// delay by 0.5 sec
    };

    this.req_server = function( params )
    {
	var txt = gk_request_remote_data( this.data_url, 0, null, params.join('&') );

	if (!txt) return [];	// page reload?

	var data = txt.split('\n');
	if (data[0]!='data-save.1.0') return [ 0, 'Connection error' ];

	var code = to_int(data[1]);
	var msg = data[2];

	data.splice( 0, 3 );
	var rest = data.join('\n');

	return [ code, msg, rest ];
    };

    this.element_enable = function( b_enable )
    {
	var el = this.get_el();
	if (!el) return;

	var me = this;    
	if (this.el_type==1)	// checkbox
	{
	    if (b_enable)
	    {
		el.disabled = false;
		el.onclick = function() { me.notify_click(el); }	// (this)
	    }
	    else
	    {
		el.disabled = true;
		el.onclick = function() {}	// nothing
	    }
	}
    };

    this.element_set = function( data )
    {
	var el = this.get_el();
	if (!el) return;

	if (this.el_type==1)	// checkbox
	{
	    el.checked = data ? true : false;
	}
    };

    this.callback_load = function ( id, txt )
    {
	if (!txt) return;	// page reload?

	var data = txt.split('\n');
	if (data.length<2) return;
	if (data[0]!='data-load.1.0') return;

	this.element_set( to_int(data[1]) );

	gui_pop_message_remove();

	this.b_loaded = 1;
	this.update();
    };

    this.update = function( b_wait )
    {
	var el = this.get_el();
	if (!el) return;

	if (this.load_params && !this.b_loaded)
	{
	    this.element_enable( 0 );
	    gui_pop_message( '<span class=span-wait></span>', el, 1000 );

	    var me = this;
	    gk_request_remote_data( this.data_url, 0, function(id,txt) { me.callback_load(id,txt); }, this.load_params.join('&') );
	    
	    this.b_loaded = 2;
	    return;
	}
	if (this.b_loaded!=1) return;

	this.element_enable( b_wait ? 0 : 1 );

	if (b_wait)
	{
	    gui_pop_message( '<span class=span-wait>Saving...</span>', el, 99000, 99000, 1 );
	    this.b_modal_msg = 1;
	}
	else
	{
	    if (this.b_modal_msg) gui_pop_message_remove();
	    this.b_modal_msg = 0;
	}

    };

    var me = this;
    setTimeout( function() 
	{
	    var o = me.get_el();
	    if (o) o.gk_data_save = me;
	    me.update();
	}, 10 );
}
