Ext.define('Ext.view.dataMaintenance.permission.ShopVersionForm', {
    extend:'Ext.form.Panel',
    bodyPadding:5,
    width:400,
    alias:'widget.shopVersionForm',
    store:'Ext.store.dataMaintenance.ShopVersions',
    layout:'anchor',
    defaults:{
        anchor:'100%'
    },
    fieldDefaults:{
        labelWidth:125,
        msgTarget:'side',
        autoFitErrors:false
    },

    // Reset and Submit buttons
    buttons:[
        {
            text:'重置',
            tooltip:"重置",
            handler:function () {
                this.up("form").form.reset();
            }
        },
        {
            text:'保存',
            action:'save'
        }
    ],
    initComponent:function () {
        var me = this;
        Ext.apply(me, {
            items:[
                {
                    fieldLabel:'name',
                    name:'name',
                    xtype:"textfield",
                    allowBlank:false
                },
                {
                    fieldLabel:'value',
                    name:'value',
                    xtype:"textfield",
                    allowBlank:false
                },
                {
                    xtype:"hiddenfield",
                    name:'id'
                }
            ]
        });
        this.callParent();
    }
});