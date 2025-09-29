
import { type DialogModel } from "@/type/BaseType";
import { reactive } from "vue";

export default function useDialog(){
    const dialog = reactive<DialogModel>({
        //弹窗属性
        title: '编辑',
        visible: false,
        width: 1200,
        height: 300,
    })
    const onShow = () => {
        dialog.visible = true;
    }
    const onClose = () => {
        console.log("useOnClose")
        dialog.visible = false;
    }
    const onConfirm = () => {
        dialog.visible = false;
    }
    return{
        dialog,onShow,onClose,onConfirm
    }
}